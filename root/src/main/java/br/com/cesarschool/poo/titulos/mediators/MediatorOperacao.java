package br.com.cesarschool.poo.titulos.mediators;

import br.com.cesarschool.poo.titulos.entidades.Transacao;
import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;
import br.com.cesarschool.poo.titulos.entidades.Acao;
import br.com.cesarschool.poo.titulos.entidades.TituloDivida;
import br.com.cesarschool.poo.titulos.repositorios.RepositorioTransacao;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/*
 * Deve ser um singleton.
 * 
 * Deve ter um atributo repositorioEntidadeOperadora, do tipo RepositorioEntidadeOperadora, que deve 
 * ser inicializado na sua declaração. Este atributo será usado exclusivamente
 * pela classe, não tendo, portanto, métodos set e get.
 * 
 * Métodos: 
 * 
 * pivate String validar(EntidadeOperadora): deve validar os dados do objeto recebido nos seguintes termos: 
 * identificador: deve ser maior que zero e menor que 100000 (1)
 * nome: deve ser preenchido, diferente de branco e de null (2). deve ter entre 5 e 60 caracteres (3).
 * data de validade: deve ser maior do que a data atual mais 180 dias (4). 
 * valorUnitario: deve ser maior que zero (5). 
 * O método validar deve retornar null se o objeto estiver válido, e uma mensagem pertinente (ver abaixo)
 * se algum valor de atributo estiver inválido.
 * 
 * (1) - Identificador deve estar entre 100 e 1000000.
 * (2) - Nome deve ser preenchido.
 * (3) - Nome deve ter entre 10 e 100 caracteres.
 *
 * public String incluir(EntidadeOperadora entidade): deve chamar o método validar. Se ele 
 * retornar null, deve incluir entidade no repositório. Retornos possíveis:
 * (1) null, se o retorno do validar for null e o retorno do incluir do 
 * repositório for true.
 * (2) a mensagem retornada pelo validar, se o retorno deste for diferente
 * de null.
 * (3) A mensagem "Entidade já existente", se o retorno do validar for null
 * e o retorno do repositório for false. 
 *
 * public String alterar(EntidadeOperadora entidade): deve chamar o método validar. Se ele 
 * retornar null, deve alterar entidade no repositório. Retornos possíveis:
 * (1) null, se o retorno do validar for null e o retorno do alterar do 
 * repositório for true.
 * (2) a mensagem retornada pelo validar, se o retorno deste for diferente
 * de null.
 * (3) A mensagem "Entidade inexistente", se o retorno do validar for null
 * e o retorno do repositório for false.
 * 
 * public String excluir(int identificador): deve validar o identificador. 
 * Se este for válido, deve chamar o excluir do repositório. Retornos possíveis:
 * (1) null, se o retorno do excluir do repositório for true.
 * (2) A mensagem "Entidade inexistente", se o retorno do repositório for false
 * ou se o identificador for inválido.
 * 
 * public EntidadeOperadora buscar(int identificador): deve validar o identificador.
 * Se este for válido, deve chamar o buscar do repositório, retornando o 
 * que ele retornar. Se o identificador for inválido, retornar null. 
 */

public class MediatorOperacao {
    private static MediatorOperacao instancia;
    private final MediatorAcao mediatorAcao = MediatorAcao.getInstancia();
    private final MediatorTituloDivida mediatorTituloDivida = MediatorTituloDivida.getInstancia();
    private final MediatorEntidadeOperadora mediatorEntidadeOperadora = MediatorEntidadeOperadora.getInstancia();
    private final RepositorioTransacao repositorioTransacao = new RepositorioTransacao();
    private MediatorOperacao() {}
    public static MediatorOperacao getInstancia() {
        if (instancia == null) {
            instancia = new MediatorOperacao();
        }
        return instancia;
    }

    public String realizarOperacao(boolean ehAcao, int idEntidadeCredito, int idEntidadeDebito, int idAcaoOuTitulo, double valor) {
        if (valor <= 0) {
            return "Valor inválido";
        }
        EntidadeOperadora entidadeCredito = mediatorEntidadeOperadora.buscar(idEntidadeCredito);
        if (entidadeCredito == null) {
            return "Entidade crédito inexistente";
        }
        EntidadeOperadora entidadeDebito = mediatorEntidadeOperadora.buscar(idEntidadeDebito);
        if (entidadeDebito == null) {
            return "Entidade débito inexistente";
        }

        if (ehAcao) {
            if (entidadeCredito.getAutorizacao() <= 0) {
                return "Entidade de crédito não autorizada para ação";
            }if (entidadeDebito.getAutorizacao() <= 0) {
                return "Entidade de débito não autorizada para ação";
            }
            Acao acao = mediatorAcao.buscar(idAcaoOuTitulo);
            if (acao == null) {
                return "Ação inexistente";
            }if (entidadeDebito.getSaldoAcao() < valor) {
                return "Saldo da entidade débito insuficiente";
            }if (acao.getValorUnitario() > valor) {
                return "Valor da operação é menor do que o valor unitário da ação";
            }
            entidadeCredito.creditarSaldoAcao(valor);
            entidadeDebito.debitarSaldoAcao(valor);
        } else {
            TituloDivida titulo = mediatorTituloDivida.buscar(idAcaoOuTitulo);
            if (titulo == null) {
                return "Título inexistente";
            }if (entidadeDebito.getSaldoTituloDivida() < valor) {
                return "Saldo da entidade débito insuficiente";
            }
            double valorOperacao = titulo.calcularPrecoTransacao(valor);

            entidadeCredito.creditarSaldoTituloDivida(valorOperacao);
            entidadeDebito.debitarSaldoTituloDivida(valorOperacao);
        }
        String mensagemAlterarCredito = mediatorEntidadeOperadora.alterar(entidadeCredito);
        if (mensagemAlterarCredito != null) {
            return mensagemAlterarCredito;
        }
        String mensagemAlterarDebito = mediatorEntidadeOperadora.alterar(entidadeDebito);
        if (mensagemAlterarDebito != null) {
            return mensagemAlterarDebito;
        }
        Transacao transacao = new Transacao(
                entidadeCredito,
                entidadeDebito,
                ehAcao ? mediatorAcao.buscar(idAcaoOuTitulo) : null,
                ehAcao ? null : mediatorTituloDivida.buscar(idAcaoOuTitulo),
                valor,
                LocalDateTime.now()
        );
        repositorioTransacao.incluir(transacao);
        return null;
    }

    public Transacao[] gerarExtrato(int idEntidade) {
        Transacao[] transacoesCredito = repositorioTransacao.buscarPorEntidadeCredora(idEntidade);
        Transacao[] transacoesDebito = repositorioTransacao.buscarPorEntidadeDebito(idEntidade);
        List<Transacao> todasTransacoes = new ArrayList<>();
        todasTransacoes.addAll(Arrays.asList(transacoesCredito));
        todasTransacoes.addAll(Arrays.asList(transacoesDebito));
        todasTransacoes.sort(Comparator.comparing(Transacao::getDataHoraOperacao).reversed());
        return todasTransacoes.toArray(new Transacao[todasTransacoes.size()]);
    }
}

package br.com.cesarschool.poo.titulos.entidades;

import java.time.LocalDate;

/*
 * Esta classe deve herdar de Ativo.
 * E deve ter os seguintes atributos:
 * valorUnitario, do tipo double.
 * 
 * Deve ter um construtor p�blico que inicializa os atributos, 
 * e m�todos set/get p�blicos para os atributos.
 * 
 * Deve ter um m�todo p�blico double calcularPrecoTransacao(double montante): o pre�o 
 * da transa��o � o produto do montante pelo valorUnitario.
 */

//atributos de Ativo: identificador, nome, dataValidade
public class Acao extends Ativo{
private double valorUnitario;

public Acao(int identificador, String nome, LocalDate dataValidade, double valorUnitario){
super(identificador, nome, dataValidade);
this.valorUnitario=valorUnitario;
}

public double calcularPrecoTransacao(double montante){
    return montante*valorUnitario;
}

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    @Override
    public String toString() {
        return this.getNome()+ " - "+"R$"+this.getValorUnitario();
    }
}

package jogodavelha;

public class Jogo {

    private String jogador1;
    private String jogador2;
    private String jogadorDaVez;
    private String jogadorAnterior;
    private String vencedor;
    private int[][] cerquilha = new int[3][3];
    private boolean comecou = false;

    public Jogo() {
    }

    public String getJogador1() {
        return jogador1;
    }

    public void setJogador1(String jogador1) {
        this.jogador1 = jogador1;
    }

    public String getJogador2() {
        return jogador2;
    }

    public void setJogador2(String jogador2) {
        this.jogador2 = jogador2;
    }

    public int[][] getCerquilha() {
        return cerquilha;
    }

    public void setCerquilha(int[][] cerquilha) {
        this.cerquilha = cerquilha;
    }

    public boolean isComecou() {
        return comecou;
    }

    public void setComecou(boolean comecou) {
        this.comecou = comecou;
    }

    public String getJogadorDaVez() {
        return jogadorDaVez;
    }

    public void setJogadorDaVez(String jogadorDaVez) {
        this.jogadorDaVez = jogadorDaVez;
    }

    public String getVencedor() {
        return vencedor;
    }

    public void setVencedor(String vencedor) {
        this.vencedor = vencedor;
    }

    public String getJogadorAnterior() {
        return jogadorAnterior;
    }

    public void setJogadorAnterior(String jogadorDaAnterior) {
        this.jogadorAnterior = jogadorDaAnterior;
    }
    
}

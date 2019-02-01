package jogodavelha;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JogoServidor extends UnicastRemoteObject implements JogoInteface {

    private static List<String> loggers = new ArrayList<>();
    private static Jogo jogo = new Jogo();

    public JogoServidor() throws RemoteException {
        super();
    }

    @Override
    public String autenticar(String nome) throws RemoteException {
        loggers.add(new Date().toString() + " - autenticar usuario");
        System.out.println("entrou no metodo" + nome);
        if (jogo.getJogador1() == null || jogo.getJogador1().isEmpty()) {
            jogo.setJogador1(nome);
            return nome + ", Voce eh o jogador 1";
        } else if (jogo.getJogador2() == null || jogo.getJogador2().isEmpty()) {
            jogo.setJogador2(nome);
            jogo.setComecou(true);
            jogo.setJogadorDaVez(jogo.getJogador1());
            jogo.setJogadorAnterior(jogo.getJogador1());
            comecou();
            return nome + ", Voce eh o jogador 2";
        } else {
            return "Voce nao pode jogar nesse monento";
        }
    }

    @Override
    public boolean minhaVez(String nome) throws RemoteException {
        loggers.add(new Date().toString() + " - Eh Minha Vez: " + nome);
        return nome.equals(jogo.getJogadorDaVez());
    }

    @Override
    public Boolean fazerJogada(int linha, int coluna) throws RemoteException {
        loggers.add(new Date().toString() + " - Fazer Jogada");
        if ((linha < 0 || linha > 2) || (coluna < 0 || coluna > 2)) {
            return false;
        }
        if (jogo.getCerquilha()[linha][coluna] != 1 && jogo.getCerquilha()[linha][coluna] != 2) {
            if (jogo.getJogadorDaVez().equals(jogo.getJogador1())) {
                jogo.getCerquilha()[linha][coluna] = 1;
                jogo.setJogadorDaVez(jogo.getJogador2());
                jogo.setJogadorAnterior(jogo.getJogador1());
                return true;
            } else if (jogo.getJogadorDaVez().equals(jogo.getJogador2())) {
                jogo.getCerquilha()[linha][coluna] = 2;
                jogo.setJogadorDaVez(jogo.getJogador1());
                jogo.setJogadorAnterior(jogo.getJogador2());
                return true;
            }
        }
        return false;
    }

    @Override
    public String desenharMatriz() throws RemoteException {
        loggers.add(new Date().toString() + " - Desenhar Matriz");
        String mensagem;
        String mat = "";

        if (vencedor(this.jogo.getCerquilha())) {
            this.jogo.setVencedor(this.jogo.getJogadorAnterior());
            System.out.println("jogador da vez" + this.jogo.getJogadorDaVez());
            System.out.println("vencedor " + this.jogo.getVencedor());
            mat = "Vencedor: " + this.jogo.getVencedor() + "\n";
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.jogo.getCerquilha()[i][j] == 1 || this.jogo.getCerquilha()[i][j] == 2) {
                    mat += this.jogo.getCerquilha()[i][j] + " | ";
                } else {
                    mat += "0 | ";
                }
            }
            mat += "\n";
        }
        return mat;
    }

    @Override
    public boolean oJogoAcabou() throws RemoteException {
        loggers.add(new Date().toString() + " - O Jogo Acabou");
        return jogo.getVencedor() != null;
    }
    ////////////////////////////////////////////////
    /////FIM MÉTODOS REMOTOS
    /////////////////////////////////////////////////

    ////////////////////////////////////////////////
    /////INICIO MÉTODOS LOCAIS
    /////////////////////////////////////////////////
    public boolean vencedor(int[][] jogo) {
        return (jogo[0][0] == jogo[0][1] && jogo[0][1] == jogo[0][2])
                || (jogo[1][0] == jogo[1][1] && jogo[1][1] == jogo[1][2])
                || (jogo[2][0] == jogo[2][1] && jogo[2][1] == jogo[2][2])
                || (jogo[0][0] == jogo[1][0] && jogo[1][0] == jogo[2][0])
                || (jogo[0][1] == jogo[1][1] && jogo[1][1] == jogo[2][1])
                || (jogo[0][2] == jogo[1][2] && jogo[1][2] == jogo[2][2])
                || (jogo[0][0] == jogo[1][1] && jogo[1][1] == jogo[2][2])
                || (jogo[0][2] == jogo[1][1] && jogo[1][1] == jogo[2][0]);
    }

    private void comecou() {
        int contador = 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                jogo.getCerquilha()[i][j] = contador;
                contador++;
            }
        }
    }

    private static void iniciarServidor() {
        try {
            System.out.println("Iniciando Jogo");
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            JogoServidor jogoServidor = new JogoServidor();
            System.out.println("Conectando o objeto Jogo no Registry...");
            Naming.rebind("rmi://localhost:1099/JogoInterface", jogoServidor); // registra o objeto "hello"
            System.out.println("Pronto para receber chamadas RMI do servidor 1...");
        } catch (MalformedURLException | RemoteException e) {
            System.out.println("Erro: " + e);
        }
    }
    ////////////////////////////////////////////////
    /////FIM MÉTODOS LOCAIS
    /////////////////////////////////////////////////

    public static void main(String[] args) {
        iniciarServidor();
        while (true) {
            try {
                Thread.sleep(9000);
                if (jogo.getVencedor() != null) {
                    jogo = new Jogo();
                    loggers.add(new Date().toString() + " - Iniciando Novo Jogo");
                }
                loggers.forEach((l) -> {
                    System.out.println(l);
                });
                loggers = new ArrayList<>();
            } catch (InterruptedException ex) {
                Logger.getLogger(JogoServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

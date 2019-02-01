package jogodavelha;

import java.net.MalformedURLException;
import java.rmi.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JogoClient {

    private static JogoInteface jogo;
    private static String nome = new String();
    private static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {

        boolean jogada = false;

        try {
            jogo = (JogoInteface) Naming.lookup("rmi://localhost:1099/JogoInterface");
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.out.println("Ocorreu um erro ao conectar ao servido 1");
            System.out.println("Tentando se conectar ao servidor 2");
            try {
             jogo = (JogoInteface) Naming.lookup("rmi://localhost:2579/JogoInterface");   
            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                Logger.getLogger(JogoClient.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        try {
            System.out.println("Digite seu nome: ");
            nome = teclado.nextLine();
            String message = jogo.autenticar(nome);
            System.out.println(message);
            if (!message.equals("Voce nao pode jogar nesse monento")) {
                while (isMinhaVez(nome)) {
                    if (!jogo.oJogoAcabou()) {
                        while (!jogada) {
                            System.out.println("Jogo atual");
                            System.out.println(jogo.desenharMatriz());
                            System.out.println("");
                            System.out.println("digite o valor da linha: ");
                            int linha = teclado.nextInt();
                            System.out.println("digite o valor da coluna: ");
                            int coluna = teclado.nextInt();
                            jogada = jogo.fazerJogada(linha, coluna);
                            System.out.println("");
                            if (jogada) {
                                System.out.println(jogo.desenharMatriz());
                            } else {
                                System.out.println("Posição ocupada, Jogue novamente.");
                            }
                        }
                        jogada = false;
                    } else {
                        System.out.println(jogo.desenharMatriz());
                        break;
                    }
                }
            }

        } catch (RemoteException e) {
            System.out.println("ERROR: " + e);
        }
    }

    private static boolean isMinhaVez(String nome) throws RemoteException {

        if (jogo.oJogoAcabou()) {
            return true;
        }
        System.out.println("Aguarde sua vez, " + nome + "...");
        System.out.println("");
        while (!jogo.minhaVez(nome)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        return true;
    }
}

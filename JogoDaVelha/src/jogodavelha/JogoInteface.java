package jogodavelha;

import java.rmi.*;

public interface JogoInteface extends Remote{
    
    public String autenticar(String nome) throws java.rmi.RemoteException;
    
    public boolean minhaVez(String nome) throws java.rmi.RemoteException;
    
    public boolean oJogoAcabou() throws java.rmi.RemoteException;
    
    public Boolean fazerJogada(int linha, int coluna)throws java.rmi.RemoteException;
    
    public String desenharMatriz()throws java.rmi.RemoteException;
}

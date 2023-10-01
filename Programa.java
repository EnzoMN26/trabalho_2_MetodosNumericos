import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.File;

public class Programa{

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        FileReader fr;
        System.out.println("Digite qual arquivo voce deseja ler:");
        String path = sc.nextLine().trim() + ".txt";
        File file = new File(path);
        

        try {

            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            int lines = countLines(path);
            Double[][] matriz = new Double[lines][lines];
            
            while((line = br.readLine()) != null){
                String[] aux = line.split(" : ");
                int emissora = Integer.parseInt(aux[0]); 
                String[] receptoras = aux[1].trim().split(" ");
                double chance = (1.0 / receptoras.length) * 0.9;
                
                for(int i = 0; i < lines; i++){
                    matriz[i][emissora-1] = 0.;
                }
                
                for(int i = 0; i < receptoras.length; i++){
                    matriz[Integer.parseInt(receptoras[i])-1][emissora-1] = chance;               
                }
            }

            for(int i = 0; i < lines; i++){
                matriz[i][i] = -1.;
            }

            double[] resposta = gaussSiedel(matriz);
            double maiorvalor = 0;
            int velhaMaisFofoqueira = 0;

            for(int i = 0; i < resposta.length; i++){
                double x = resposta[i];
                if( x > maiorvalor){
                    maiorvalor = x;
                    velhaMaisFofoqueira = i + 1;
                }
            }

            System.out.println("A velinha " + velhaMaisFofoqueira + " recebeu a maior quantidade de fofocas: " + maiorvalor);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int countLines(String file){

        try (Stream<String> fileStream = Files.lines(Paths.get(file))) {

  	        return (int) fileStream.count();

        } catch( Exception e){

            e.printStackTrace();

        }
        return 0;
    }

    public static double[] gaussSiedel(Double[][] matriz) {
        Integer[] b = new Integer[matriz[0].length];
        double tolerancia = 1e-6;
        int maxIterations = 100;
        int n = matriz.length;
        double[] x = new double[n];
        double[] prevX = new double[n];

        // Popula o vetor b
        for(int i = 0; i < b.length; i++){
            b[i] = -1;
        }

        // Inicialize x com zeros
        for (int i = 0; i < n; i++) {
            x[i] = 0.0;
        }

        int iteration = 0;
        while (iteration < maxIterations) {
            // Salva o valor atual de x para verificar a convergência
            System.arraycopy(x, 0, prevX, 0, n);

            for (int i = 0; i < n; i++) {
                double sigma = 0.0;
                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        sigma += matriz[i][j] * x[j];
                    }
                }
                x[i] = (b[i] - sigma) / matriz[i][i];
            }

            iteration++;

            // Verifique a convergência comparando com a tolerância
            boolean converged = true;
            for (int i = 0; i < n; i++) {
                if (Math.abs(x[i] - prevX[i]) > tolerancia) {
                    converged = false;
                    break;
                }
            }

            if (converged) {
                break;
            }
        }

        return x;
    }
}
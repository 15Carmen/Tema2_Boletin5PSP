import java.util.concurrent.Semaphore;

public class ColaCharcuteria implements Runnable{

    /*
    A la carnicería del ejercicio anterior le ha ido bien y ha ampliado el local, añadiendo también una sección de
    Charcutería. Como el sistema de hilos que implementaste le gustó, te ha pedido que lo vuelvas a implementar pero
    añadiendo la Charcutería. Ten en cuenta lo siguiente para realizarlo:

        - En la Carnicería sigue habiendo 4 personas trabajando, pero en la Charcutería solo 2.
        - Todos los clientes van a pasar tanto por la Charcutería como por la Carnicería.
        - Si la Carnicería está llena (es decir, ya están las 4 personas ocupadas) pero en la Charcutería hay algún
            trabajador libre, el cliente que esté esperando para ser atendido por Charcutería será atendido. Y viceversa,
            si en la Charcutería están todos ocupados, pero en la Carnicería hay alguien libre, y algún cliente está
            pendiente de ser atendido por Carnicería, puede ser atendido por ella.
        - Un cliente está completamente servido cuando ha sido atendido tanto por Carnicería como por Charcutería,
            y no siempre tiene que ser en este orden.

    El método main debe lanzar 10 hilos. Establece un nombre para cada hilo. Escribe mensajes tanto para cuando el
    cliente esté siendo atendido en Carnicería como si está siendo atendido en Charcutería.
     */

    public static Semaphore semaforoCarniceria = new Semaphore(4);
    public static Semaphore semaforoCharcuteria = new Semaphore(2);


    public static void carniceria(){

        try {
            semaforoCarniceria.acquire();
            System.out.println("El "+ Thread.currentThread().getName() +" esta siendo atendido en la carniceria");
            Thread.sleep(1000);
            System.out.println("El "+ Thread.currentThread().getName() +" ha terminado en la carniceria");
            semaforoCarniceria.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void charcuteria(){

        try {
            semaforoCharcuteria.acquire();
            System.out.println("El "+ Thread.currentThread().getName() +" esta siendo atendido en la charcuteria");
            Thread.sleep(1000);
            System.out.println("El "+ Thread.currentThread().getName() +" ha terminado en la charcuteria");
            semaforoCharcuteria.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        boolean atendidoCarniceria =false;
        boolean atendidoCharcuteria =false;

        while(!atendidoCarniceria && !atendidoCharcuteria) {
            if(semaforoCarniceria.availablePermits()>0 && !atendidoCarniceria) {
                carniceria();
                if (!atendidoCharcuteria) {
                    charcuteria();
                    atendidoCharcuteria =true;
                }

            }else if (semaforoCharcuteria.availablePermits()>0 && !atendidoCharcuteria){
                charcuteria();
                if (!atendidoCarniceria){
                    carniceria();
                    atendidoCarniceria =true;
                }
            }
        }

    }

    public static void main(String[] args) {

        ColaCharcuteria cc = new ColaCharcuteria();
        for (int i = 1; i <= 10; i++) {
            Thread hilo = new Thread(cc);
            hilo.setName("cliente "+i);
            hilo.start();
        }
    }


}
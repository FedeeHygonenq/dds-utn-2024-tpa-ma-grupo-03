package ar.edu.utn.frba.dds.cronJobs;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CronInitializer {
  public static void initCronJobs() {
    // Crea un ScheduledExecutorService con un tamaño de pool
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    // Cron job 1: Guardar registros de sensores cada 5 minutos
    scheduler.scheduleAtFixedRate(new MainGuardarRegistrosDeSensores(), 1, 5, TimeUnit.MINUTES);

    // Cron job 2: Comprobar conexión de sensores cada 5 minutos
    scheduler.scheduleAtFixedRate(new MainComprobarConexionesSensores(), 0, 5, TimeUnit.MINUTES);

    // Cron job 3: Tarea para limpiar las solicitudes de apertura caducadas cada día a las 00:00
    scheduleDailyTask(scheduler, new MainLimpiarSolicitudesDeAperturaCaducadas());

    // Cron job 4: Generar reportes cada 1 semana a las 00:00 del lunes
    scheduleWeeklyTask(scheduler, new MainReportesSemanales());

    // Asegura que el scheduler se detiene cuando la aplicación se cierra
    Runtime.getRuntime().addShutdownHook(new Thread(scheduler::shutdown));
  }

  private static void scheduleDailyTask(ScheduledExecutorService scheduler, Runnable task) {
    // Calcular el retraso hasta las 00:00 del siguiente día
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    // Si ya pasó la medianoche de hoy, programar para mañana
    if (Calendar.getInstance().after(calendar)) {
      calendar.add(Calendar.DAY_OF_MONTH, 1);
    }

    long initialDelay = calendar.getTimeInMillis() - System.currentTimeMillis();
    long period = TimeUnit.DAYS.toMillis(1); // 1 día en milisegundos

    scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
  }

  private static void scheduleWeeklyTask(ScheduledExecutorService scheduler, Runnable task) {
    // Calcular el retraso hasta el próximo lunes a las 00:00
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    // Si ya pasó el lunes de esta semana, programar para el próximo lunes
    if (Calendar.getInstance().after(calendar)) {
      calendar.add(Calendar.WEEK_OF_YEAR, 1);
    }

    long initialDelay = calendar.getTimeInMillis() - System.currentTimeMillis();
    long period = TimeUnit.DAYS.toMillis(7); // 1 semana en milisegundos

    scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
  }
}

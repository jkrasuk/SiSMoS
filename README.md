# Proyecto Sistemas Operativos Avanzados 1C 2020

### Integrantes

- Darío Femenía - 33304860
- Joaquín Krasuk - 40745090

## Proyecto SiSMoS

La propuesta consiste en un sistema de señalización de movimientos sísmicos orientado especialmente a los periodos nocturnos. Para llevar a cabo esta tarea, se utilizará el sensor de luz del dispositivo (para detectar que el lugar donde se encuentra el celular se encuentra a oscuras), el acelerómetro (al ocurrir un movimiento sísmico, existirá una aceleración en los distintos ejes, según la dirección del mismo) y el giroscópio (para controlar que no hay rotaciones).

Una vez que se detecta un cierto nivel de movimiento, comenzará a sonar una alarma al máximo volumen posible. Además, se mostrará por pantalla una serie de indicaciones simples y necesarias hasta que finalice el movimiento. Después de que el mismo finalice, la pantalla de bienvenida se vuelve a mostrar nuevamente. Si se desea, se podrá visualizar en la pantalla de historial de alarmas el tipo de movimiento que ocurrió, así como la fecha y hora de comienzo.

Además de la funcionalidad de detección, también se incorpora una pantalla de configuración, en la cual será posible determinar el sonido de alarma deseado (dada una lista) y la posición manual (la misma en primera instancia se detectará utilizando el GPS, pero en caso de que el usuario deseé, puede determinar otra).

Por último, se integra una funcionalidad que permite consultar los movimientos sísmicos reportados por el INPRES (Instituto Nacional de Prevención Sísmica), obteniendo los datos del sitio web. Al pulsar uno de estos sismos, se mostrará información como localización en el mapa, cantidad de Km. entre el epicentro del evento y la ubicación actual (o la establecida manualmente), profundidad del evento, magnitud, latitud y longitud, así como la fecha y hora.

# Bouncing Ball — animación y concurrencia en Java Swing

Proyecto didáctico que representa una pelota roja rebotando en una ventana mediante **Java Swing** y **Java 2D**. La versión incluida en el repositorio utiliza **dos hilos de trabajo**: uno actualiza la posición de la pelota y otro solicita el repintado periódico. Las teclas **I** y **O** permiten aumentar y disminuir su tamaño.

## Tecnologías y requisitos

- Java (código compilado satisfactoriamente con **OpenJDK 21**).
- Swing y Java 2D (incluidos en el JDK; se requiere entorno gráfico).
- Gradle, con *wrapper* configurado para **Gradle 9.6.0**.
- JUnit Jupiter **6.0.0** configurado como dependencia de pruebas; el ZIP no incluye clases de prueba.

## Estructura del proyecto

```text
bouncing_ball/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── gradle/wrapper/
│   └── gradle-wrapper.properties
└── src/
    ├── main/java/mx/unison/
    │   ├── Main.java
    │   ├── Board.java
    │   └── BallAnimator.java
    ├── main/resources/
    └── test/resources/
```

## Descripción de las clases

### `Main.java`

Punto de entrada de la aplicación (`public static void main(String[] args)`). Invoca `SwingUtilities.invokeLater(...)` para construir la interfaz en el **Event Dispatch Thread (EDT)**. Su método privado `createAndShowGUI()`:

1. Crea un `JFrame` con título `Bouncy Ball`.
2. Crea un `JPanel` con `BorderLayout` y agrega un `Board` al centro.
3. Registra `Board` como `KeyListener` del `JFrame`.
4. Configura el cierre de la ventana, ajusta su tamaño con `pack()` y la muestra.

### `Board.java`

Componente visual que extiende `JComponent` e implementa `Runnable` y `KeyListener`.

- **Constructor `Board()`**: configura borde y opacidad, crea una `Ellipse2D.Double` de 20 × 20 y pone en marcha `BounceThread` y `RefreshThread`.
- **`paintComponent(Graphics)`**: dibuja el fondo y la pelota roja usando `Graphics2D.fill(...)`.
- **`getPreferredSize()`**: proporciona inicialmente un tamaño de 640 × 480 píxeles.
- **`run()`**: solicita `repaint()` en un ciclo continuo, aproximadamente cada 100 ms.
- **`zoomIn()` / `zoomOut()`**: modifican ancho y alto de la elipse multiplicándolos por 1.1 o 0.9; utilizan comprobaciones de tamaño antes de cambiarlo.
- **`keyTyped(KeyEvent)`**: interpreta `I/i` para aumentar y `O/o` para disminuir el tamaño. `keyPressed` y `keyReleased` están vacíos.

### `BallAnimator.java`

Implementa `Runnable` y actualiza la posición de la pelota.

- **Constructor `BallAnimator(Shape)`**: convierte el argumento a `Ellipse2D.Double`, conserva la referencia y establece la posición inicial en (320, 240). Esta conversión presupone que el objeto recibido es de ese tipo.
- **`run()`**: realiza un ciclo infinito; invierte la dirección cuando detecta que las coordenadas rebasaron límites definidos y avanza 10 píxeles por eje por iteración, con una pausa de 150 ms.
- **Constantes**: `MAX_X = 640`, `MAX_Y = 480`, `DX = 10`, `DY = 10`, `SIGN = -1`.

Los límites se calculan con un diámetro fijo de 20 píxeles y no con el tamaño real de la pelota o del componente.

## Flujo de ejecución

```text
Main.main()
  └── SwingUtilities.invokeLater(...)
       └── Main.createAndShowGUI()
            └── new Board()
                 ├── new BallAnimator(ball) → BounceThread.start()
                 │    └── modifica las coordenadas de la elipse
                 └── RefreshThread.start()
                      └── repaint() cada ~100 ms

EDT → Board.paintComponent(...) → dibuja la elipse
Teclado I/O → Board.keyTyped(...) → cambia sus dimensiones
```

**Nota de concurrencia:** la misma instancia mutable `Ellipse2D.Double` es utilizada por el hilo de animación y por el EDT sin sincronización explícita. Este diseño es útil para discutir problemas de estado compartido, pero no garantiza acceso coherente a sus propiedades.

## Compilación y ejecución

Desde la raíz del proyecto:

**Linux/macOS**

```bash
./gradlew build
```

**Windows (PowerShell)**

```powershell
.\gradlew.bat build
```

El proyecto aplica el plugin `java`, pero **no configura** el plugin `application` ni una tarea `run`. Para iniciar la aplicación desde la línea de comandos, después de compilar:

**Linux/macOS**

```bash
java -cp build/classes/java/main mx.unison.threads.Main
```

**Windows (PowerShell)**

```powershell
java -cp build\classes\java\main mx.unison.threads.Main
```

También puede abrirse el proyecto en **IntelliJ IDEA** y ejecutar el método `main` de `mx.unison.Main`.

> La primera ejecución del wrapper puede necesitar conexión a Internet para descargar Gradle y las dependencias. La aplicación requiere una sesión de escritorio: no es apta para entornos *headless*.

## Controles

| Tecla | Acción |
|---|---|
| `I` o `i` | Aumentar el tamaño de la pelota |
| `O` o `o` | Disminuir el tamaño de la pelota |

La recepción de las teclas depende del foco de la ventana.

## Conceptos ilustrados

- Construcción de interfaces Swing en el EDT.
- Dibujo con `Graphics2D` y `Ellipse2D`.
- Implementación de `Runnable` y creación de objetos `Thread`.
- Diferencia entre actualización de estado y repintado.
- Procesamiento de eventos de teclado.
- Movimiento incremental, detección de límites y rebote.
- Riesgos de compartir objetos mutables entre hilos.

## Limitaciones 

1. No hay mecanismo explícito para detener los dos hilos al cerrar la ventana.
2. La elipse se modifica desde distintos hilos sin coordinación.
3. Los límites de rebote están fijados en 640 × 480 y no se adaptan a cambios de tamaño de la ventana.
4. El rebote usa un diámetro constante de 20 píxeles, incluso después del zoom.
5. La comprobación de límites antes de avanzar puede permitir que la pelota salga parcialmente del área visible.
6. No existen pruebas automatizadas en el archivo proporcionado.


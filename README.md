# Productos Service - Analisis SonarQube

Proyecto practico de la Unidad 10 del curso Patrones de Diseno de Software. El objetivo es configurar un proyecto Spring Boot con codigo intencionalmente imperfecto, integrar JaCoCo, ejecutar un analisis inicial con SonarQube y documentar los hallazgos principales.

## Tecnologias utilizadas

- Java 21
- Spring Boot 3.3.5
- Maven
- H2 Database
- Lombok
- JaCoCo
- SonarQube Community Edition

## Ejecucion local

Levantar SonarQube con Docker:

```bash
docker run -d \
  --name sonarqube \
  -p 9000:9000 \
  -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true \
  sonarqube:community
```

Entrar a `http://localhost:9000` con `admin / admin`, cambiar la contrasena inicial y crear el proyecto manualmente:

- Project name: `productos-service`
- Project key: `com.universidad:productos-service`

Compilar, ejecutar pruebas y generar el reporte de cobertura:

```bash
mvn clean verify
```

Ejecutar el analisis de SonarQube:

```bash
mvn sonar:sonar -Dsonar.token=TU_TOKEN
```

Tambien se puede ejecutar todo en un solo comando:

```bash
mvn clean verify sonar:sonar -Dsonar.token=TU_TOKEN
```

## Estado inicial del analisis

| Categoria | Cantidad | Rating |
|-----------|----------|--------|
| Bugs | Pendiente de dashboard | Pendiente |
| Vulnerabilidades | Pendiente de dashboard | Pendiente |
| Code Smells | Pendiente de dashboard | Pendiente |
| Cobertura | 16.7% segun JaCoCo local | - |

## Hallazgos principales identificados

### Bug 1: Retorno nulo al buscar un producto inexistente

- Archivo: `src/main/java/com/universidad/productosservice/service/ProductoService.java`
- Ubicacion: metodo `buscar(Long id)`
- Descripcion: el metodo retorna `null` cuando el producto no existe. Esto puede causar errores posteriores como `NullPointerException` en las capas que consumen el servicio.
- Severidad esperada: Major

### Code Smell 1: Inyeccion de dependencia por campo

- Archivo: `src/main/java/com/universidad/productosservice/service/ProductoService.java`
- Ubicacion: atributo `repo`
- Descripcion: el servicio usa `@Autowired` directamente sobre el campo. Esta practica reduce la facilidad de prueba y hace menos explicitas las dependencias de la clase.
- Severidad esperada: Major

### Code Smell 2: Metodo con complejidad ciclomática alta

- Archivo: `src/main/java/com/universidad/productosservice/domain/Producto.java`
- Ubicacion: metodo `getEstado()`
- Descripcion: el metodo contiene varias condiciones encadenadas para clasificar el stock. Segun la guia de la Unidad 10, cada punto de decision incrementa la complejidad ciclomática y exige mas casos de prueba independientes.
- Severidad esperada: Major

### Code Smell 3: Parametros no utilizados y responsabilidad incompleta

- Archivo: `src/main/java/com/universidad/productosservice/service/ProductoService.java`
- Ubicacion: metodo `procesarProducto(...)`
- Descripcion: los parametros `cat`, `activo` y `proveedor` no se usan. Esto senala una responsabilidad incompleta y puede confundir a quien mantenga el codigo.
- Severidad esperada: Minor/Major

## Capturas del dashboard

Despues de ejecutar el analisis, guardar las capturas reales en la carpeta `docs/` con estos nombres:

![alt text](<Capturas de pantalla/Sonar-Dashboard.png>)

![alt text](<Capturas de pantalla/Sonar-code-smells.png>)

![alt text](<Capturas de pantalla/Sonar-Bugs.png>)

## Interpretacion inicial

El estado inicial del proyecto debe mostrar problemas de mantenibilidad y cobertura insuficiente. Esto es intencional: el objetivo del Post-Contenido 1 no es corregir los hallazgos, sino ejecutar el analisis estatico, interpretar el dashboard y clasificar los resultados en Bugs, Vulnerabilidades, Code Smells y cobertura.

La cobertura generada por JaCoCo sera baja porque el proyecto incluye solo una prueba minima. De acuerdo con la guia de la Unidad 10, la cobertura no garantiza ausencia de bugs, pero sirve como evidencia objetiva para identificar partes del codigo que no han sido ejercitadas por pruebas automatizadas.

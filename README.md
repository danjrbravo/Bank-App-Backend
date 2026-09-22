# Sistema de Gestión Financiera

## 1. Generalidades

Desarrollar una aplicación que será utilizada por los trabajadores de una entidad financiera. Dicha aplicación permitirá la administración de los clientes, lo cual incluye el registro de clientes, la actualización de sus datos y la eliminación de los mismos.

También permitirá crear productos financieros para sus clientes. Finalmente, a estos productos financieros se les podrán realizar movimientos transaccionales y consultar los estados de cuenta.

## 2. Requerimientos funcionales

Se debe crear un CRUD de clientes, productos y transacciones y exponer un servicio REST que permita cumplir con las siguientes indicaciones:

### Clientes

* La aplicación debe permitir crear un cliente con los siguientes atributos como mínimo:

  * ID
  * Tipo de identificación
  * Número de identificación
  * Nombres
  * Apellidos
  * Correo electrónico
  * Fecha de nacimiento
  * Fecha de creación
  * Fecha de modificación

* La aplicación debe permitir modificar la información del cliente. Cuando se realiza una modificación de información de un cliente, se debe calcular esta fecha de modificación automáticamente, tomando la última fecha en la que se realiza la modificación.

* La aplicación debe permitir eliminar un cliente que ha sido creado.

* Un cliente no podrá ser creado ni existir en la base de datos si es menor de edad.

* Un cliente no podrá ser eliminado si tiene productos vinculados.

* La fecha de creación debe ser calculada automáticamente al registrar un cliente.

#### Requerimientos opcionales

* El campo correo electrónico solo debe permitir ingresar valores que correspondan con un correo electrónico, es decir, debe ser de un formato [xxxx@xxxxx.xxx](mailto:xxxx@xxxxx.xxx).

* La extensión del nombre y el apellido no puede ser menor a 2 caracteres.

### Productos (Cuentas)

* La aplicación debe permitir crear únicamente dos tipos de productos:

  * Cuenta corriente
  * Cuenta de ahorros

* Un producto financiero solo podrá existir en la base de datos si está vinculado a un cliente de la entidad financiera.

* Las cuentas corrientes o de ahorro se deben crear con los siguientes atributos como mínimo:

  * ID
  * Tipo de cuenta
  * Número de cuenta
  * Estado: activa, inactiva o cancelada
  * Saldo
  * Exenta GMF
  * Fecha de creación
  * Fecha de modificación
  * Usuario al que pertenece la cuenta

* La cuenta de ahorros no puede tener un saldo menor a $0 (cero).

* Las cuentas corrientes y de ahorros se pueden activar o inactivar en cualquier momento.

* El número de las cuentas corrientes y de ahorros debe ser único y generarse automáticamente.

* La extensión del número de cuenta debe ser de 10 dígitos numéricos.

* El número de las cuentas de ahorro debe iniciar en 53.

* El número de las cuentas corrientes debe iniciar en 33.

* Al crear una cuenta de ahorro esta debe establecerse como activa de forma predeterminada.

* Solo se podrán cancelar las cuentas que tengan un saldo igual a $0.

* La fecha de creación de cada producto debe ser calculada automáticamente al registrar el producto.

* El saldo de la cuenta deberá actualizarse al realizar cualquier transacción exitosa.

### Transacciones (Movimientos financieros)

* La aplicación debe permitir crear únicamente las siguientes transacciones:

  * Consignación
  * Retiro
  * Transferencia entre cuentas

* La aplicación debe actualizar el saldo y el saldo disponible con cada transacción realizada.

* Las transferencias solo se podrán realizar entre cuentas existentes en el sistema.

* Al realizar una transferencia se deben generar:

  * Un movimiento de crédito en la cuenta de recepción.
  * Un movimiento débito en la cuenta de envío.

* Se deben implementar las estructuras de persistencia en la base de datos necesarias para cumplir con todos los requerimientos funcionales obligatorios.

## 3. Requerimientos no funcionales

### Backend

* Se debe desarrollar como mínimo un proyecto backend.

* El proyecto backend deberá ser desarrollado utilizando Java.

* El proyecto debe utilizar una arquitectura hexagonal en lo posible o MVC (Modelo, Vista, Controlador).

* El proyecto debe utilizar una base de datos de las siguientes opciones:

  * SQL Server
  * Oracle
  * PostgreSQL
  * MySQL

* El proyecto backend se debe generar por capas utilizando al menos las siguientes:

  * Entity
  * Service
  * Controller
  * Repository

### Test unitarios

* La aplicación debe implementar test unitarios utilizando la librería JUnit con una cobertura para las capas Service y Controller.

### Control de versiones

* Se debe utilizar el motor de versionamiento Git.

* Se debe crear un solo repositorio en GitHub que tenga el código fuente del proyecto a realizar.

* Se debe evidenciar el avance del proyecto mediante commits y push.

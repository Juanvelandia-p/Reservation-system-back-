
 Sistema de Reservas de Laboratorios

Este proyecto  es para implementar un sistema de reservas de laboratorios, utilizando Spring Boot y MongoDB. El sistema permite gestionar laboratorios, realizar reservas, consultar disponibilidad y cancelar reservas, a través de una API REST.


## Diseño

![image](https://github.com/user-attachments/assets/a8674e0a-a475-47c5-b6f0-41a77eea9d39)


## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes:

- *controller*  
  Contiene los controladores con los endpoints  
  - LaboratoryController: Creación y consulta de laboratorios
  - ReservationController: Operaciones de reservas (crear, consultar, cancelar y verificar disponibilidad).

- *model*  
  Modelos que representan los datos en el sistema
  - Laboratory: Representa un laboratorio
  - Reservation: Representa una reserva de laboratorio
  - User: Representa un usuario del sistema
  - HoursRange: Representa los rangos horarios disponibles para las reservas

- *mongoConnection*  
  Repositorios que extienden MongoRepository para interactuar con MongoDB
  - LaboratoryRepository
  - ReservationRepository
  - HourRangeRepository

- *servicios*  
  Logica de negocio y las validaciones necesarias para la gestion de reservas y laboratorios
  - LaboratoryService: Métodos para agregar y consultar laboratorios
  - MakeReservationService: Metodos para hacer consultar y cancelar reservas

## Características

- *Gestión de Laboratorios:*  
  Creación y consulta de laboratorios evitando duplicados mediante validaciones

- *Gestión de Reservas:*  
  Permite realizar reservas para un laboratorio específico siempre que:
  - El laboratorio exista
  - El horario debe estar disponible
  - No debe haber otra reserva para el mismo laboratorio, fecha y hora

- *Consultas y Cancelaciones:*  
  Se pueden consultar reservas individuales o todas las reservas y también cancelar reservas existentes

- *Verificación de Disponibilidad:*  
  Permite consultar si un laboratorio se encuentra disponible en un rango horario y fecha determinados

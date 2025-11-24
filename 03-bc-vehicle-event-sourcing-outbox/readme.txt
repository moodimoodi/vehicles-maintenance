Command services :

- Exposent des API REST pour les commandes (création / modification)

- Persiste en base MySQL (modèle orienté écriture)

- Publient des Domain Events sur Kafka

Query services :

- Consomment les events Kafka

- Maintiennent des read models (vues optimisées lecture) en MySQL

- Exposent des API REST de lecture (recherche, liste, projections)

⚙️ Tech Stack

Langage : Java 17

Framework : Spring Boot 3.3.x

Persistance : Spring Data JPA + MySQL

Messaging : Apache Kafka + Spring Kafka

Mapping : MapStruct

Boilerplate : Lombok

Build : Maven

🧱 Bounded Context: Maintenance

maintenance-command-service

Responsabilités :

- CRUD / commandes de maintenance :

- planification de rendez-vous

- changement de statut (scheduled / in-progress / completed / cancelled)

- Validation métier et intégrité

Emission d’events :

- MaintenanceScheduledEvent

- MaintenanceStatusChangedEvent

- MaintenanceCompletedEvent

Base MySQL typique :

- maintenance_appointments

- maintenance_services (catalogue des types de maintenance, durée, etc.)

Kafka Topics :

- maintenance-scheduled

- maintenance-status-changed

- maintenance-completed

maintenance-query-service

Responsabilités :

- Consommation des topics de maintenance

- Projection dans des read models optimisés :

- customer_maintenance_view

- workshop_daily_schedule_view

API REST de lecture :

- lister les rendez-vous d’un client

- planning journalier d’un atelier

- détail d’un rendez-vous

🧱 Bounded Context: Vehicle
vehicleAggregate-command-service (WRITE Side)

Responsabilités :

- Enregistrement d’un véhicule

- Mise à jour (couleur, kilométrage…)

- Changement de propriétaire (customer)

Principales classes


vehicleAggregate-query-service (READ Side)

Responsabilités :

Consommer les events :

- vehicleAggregate-registered

- vehicleAggregate-updated

- vehicleAggregate-ownership-changed

Mettre à jour les vues de lecture :

- VehicleOverviewViewEntity

- CustomerGarageViewEntity

Exposer des endpoints de lecture :

Read Models

vehicle_overview_view
- Vue : 1 vehicleAggregate = 1 ligne (détail complet)

customer_garage_view
- Vue : 1 ligne par véhicule appartenant à un customer :


API REST (exemple)

- Base path : /api/vehicles/queries

- GET /api/vehicles/queries/{vin}

Détail d’un véhicule (vue read) :

- GET /api/vehicles/queries/customer/{customerId}

Liste des véhicules d’un client

📦 core-api :

Un module core-api peut être utilisé pour factoriser les éléments communs :

- DomainEventEnvelope<T>

- DTOs de base

- Events de domaine (Maintenance*Event, Vehicle*Event)

- Enums métier (ex: MaintenanceStatus, MaintenanceErrorCodes)

- Constants (topic names, bounded context names…)

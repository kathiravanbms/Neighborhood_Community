🌿 Neighbourhood Community Garden Management System

A Java + JDBC based console application to manage a community garden system.
This project handles gardeners, plot allocation, and validation using a proper layered architecture (Bean → DAO → Service → Util).

🧰 Technologies Used
    Java
    JDBC
    Oracle (any JDBC-supported DB)
Neighbourhood_Community
│
├── src
│   ├── com.garden.app
│   │      └── GardenMain.java
│   │
│   ├── com.garden.bean
│   │      ├── Gardener.java
│   │      └── PlotTaskRow.java
│   │
│   ├── com.garden.dao
│   │      ├── GardenerDAO.java
│   │      └── PlotTaskDAO.java
│   │
│   ├── com.garden.service
│   │      └── GardenService.java
│   │
│   └── com.garden.util
│          ├── DBUtil.java
│          ├── ActiveAllocationsOrPendingTasks.java
│          ├── PlotAllocationConflictException.java
│          └── ValidationException.java
│
└── README.md

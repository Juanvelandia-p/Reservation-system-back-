package edu.eci.cvds.ReservationSystem.mongoConnection;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface ReservationRepository extends MongoDatabase{
	
	
	
}


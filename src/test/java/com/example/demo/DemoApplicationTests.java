package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private MongoClient mongoClient;

    @Test
    void testMongoConnection() {
        try {
            MongoDatabase database = mongoClient.getDatabase("Garage");
            System.out.println("✅ Kết nối MongoDB thành công tới database: " + database.getName());
        } catch (Exception e) {
            System.err.println("❌ Kết nối MongoDB thất bại: " + e.getMessage());
        }
    }
}

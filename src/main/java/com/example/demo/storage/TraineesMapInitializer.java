package com.example.demo.storage;

import com.example.demo.model.Trainee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class TraineesMapInitializer {

    @Value("${trainees.file.path}")
    private String path;

    private final Map<UUID, Trainee> traineesMap ;
    private final Logger LOGGER = LoggerFactory.getLogger(TraineesMapInitializer.class);

    @Autowired
    public TraineesMapInitializer(@Qualifier("traineesMap")Map<UUID, Trainee> traineesMap) {
        this.traineesMap = traineesMap;
    }


    @PostConstruct
    public Map<UUID , Trainee> initialize()  {
        File file = new File(path);

        if(file.exists()) {
            try{
                LOGGER.info("Initializing Trainees Map");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                List<Trainee> trainers = objectMapper.readValue(new File(path) , new TypeReference<>() {});

                for (Trainee trainee : trainers) {
                    traineesMap.put(trainee.getUserId(), trainee);
                }
                LOGGER.info("Initialized Trainees Map");
            }catch (IOException e){
                LOGGER.error("Error while reading file: {}", e.getMessage());
            }
        }else LOGGER.error("File not found");

        return traineesMap;
    }



}

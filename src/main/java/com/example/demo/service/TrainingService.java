package com.example.demo.service;


import com.example.demo.dao.TraineeDAO;
import com.example.demo.dao.TrainerDAO;
import com.example.demo.dao.TrainingDAO;
import com.example.demo.dto.TrainingCreateDTO;
import com.example.demo.model.Trainee;
import com.example.demo.model.Trainer;
import com.example.demo.model.Training;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;


@Service
@Getter
public class TrainingService {

    private final TrainingDAO genericDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingService.class);

    private TrainerDAO trainerDAO;
    private TraineeDAO traineeDAO;

    public TrainingService(TrainingDAO dao){
        this.genericDao = dao;
    }

    public Training create(TrainingCreateDTO createDTO) {

        if(createDTO == null){
            throw new IllegalArgumentException();
        }

        Training newTraining = new Training();


        Optional<Trainer> trainer = trainerDAO
                                            .selectById(createDTO.getTrainerId());

        Optional<Trainee> trainee = traineeDAO
                                            .selectById(createDTO.getTrainerId());

        if(trainer.isEmpty() || trainee.isEmpty()) {
            throw new IllegalArgumentException("Trainer or trainee does not exist");
        }

        UUID id = UUID.randomUUID();  
        newTraining.setTraineeId(createDTO.getTraineeId());
        newTraining.setTrainerId(createDTO.getTrainerId());
        newTraining.setTrainingDate(createDTO.getTrainingDate());
        newTraining.setTrainingDuration(createDTO.getTrainingDuration());
        newTraining.setTrainingName(createDTO.getTrainingName());
        newTraining.setTrainingType(createDTO.getTrainingType());


        genericDao.create(id , newTraining);

        LOGGER.info("{} successfully created" , newTraining);

        return newTraining;

    }


    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void  setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }




}

package org.budgetmanager;

import org.springframework.stereotype.Service;

@Service
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;

    public TreatmentServiceImpl(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @Override
    public Treatment getTreatment(int id) {
        return treatmentRepository.getOne(id);

    }

    @Override
    public void create(Treatment treatment) {
        treatmentRepository.save(treatment);
    }

}
package org.budgetmanager;

public interface TreatmentService {

    Treatment getTreatment(int id);

    void create(Treatment treatment);
}

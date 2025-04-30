package com.chtrembl.petstore.pet.service;

import com.chtrembl.petstore.pet.model.Pet;
import com.chtrembl.petstore.pet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    // Method to get a pet by ID
    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    // Method to add a new pet
    public Pet addPet(Pet pet) {
        return petRepository.save(pet);
    }

    // Method to update an existing pet
    public Pet updatePet(Pet pet) {
        if (pet.getId() != null && petRepository.existsById(pet.getId())) {
            return petRepository.save(pet);
        }
        throw new IllegalArgumentException("Pet with ID " + pet.getId() + " does not exist.");
    }

    // Method to delete a pet by ID
    public void deletePet(Long id) {
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Pet with ID " + id + " does not exist.");
        }
    }

    // Method to find pets by status
    public List<Pet> findPetsByStatus(Pet.StatusEnum status) {
        return petRepository.findByStatus(status);
    }
}

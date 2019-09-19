package com.fleet.vehicle;

import com.fleet.exception.ConflictException;
import com.fleet.exception.NotFoundException;
import com.fleet.vehicle.validation.VehicleIdValidation;
import com.fleet.vehicle_color.VehicleColorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("vehicles")
@Validated
public class VehicleController {

    private final String INVALID_VEHICLE_ID_MESSAGE = "{vehicle.invalid_id}";
    private final String VEHICLES_LIST_CACHE = "vehiclesList";

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(cacheNames = VEHICLES_LIST_CACHE, allEntries = true)
    public VehicleDTO insert(@RequestBody final VehicleDTO vehicleDTO) throws ConflictException {
        VehicleDTO insertedVehicle = vehicleService.insert(vehicleDTO);
        return insertedVehicle;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public VehicleDTO findById(@PathVariable @VehicleIdValidation(message = INVALID_VEHICLE_ID_MESSAGE) final String id)
            throws NotFoundException {
        VehicleDTO vehicleDTO = vehicleService.findById(id);
        return vehicleDTO;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(VEHICLES_LIST_CACHE)
    public Page<VehicleDTO> list(@PageableDefault(page = 0, size = 10, sort = "id",
            direction = Sort.Direction.ASC) Pageable pagination) {
        Page<VehicleDTO> vehiclesDTOPage = vehicleService.list(pagination);
        return vehiclesDTOPage;
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(cacheNames = VEHICLES_LIST_CACHE, allEntries = true)
    public VehicleDTO changeColor(@PathVariable
                                  @VehicleIdValidation(message = INVALID_VEHICLE_ID_MESSAGE) final String id,
                                  @RequestBody final VehicleColorDTO vehicle)
            throws NotFoundException {
        VehicleDTO vehicleUpdated = vehicleService.changeColor(id, vehicle.getColor());
        return vehicleUpdated;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = VEHICLES_LIST_CACHE, allEntries = true)
    public void deleteById(@PathVariable @VehicleIdValidation(message = INVALID_VEHICLE_ID_MESSAGE) final String id)
            throws NotFoundException {
        vehicleService.deleteById(id);
    }
}
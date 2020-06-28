package de.luuuke.pcubes.controller;

import de.luuuke.pcubes.models.DimensionAttribute;
import de.luuuke.pcubes.models.DimensionElementValue;
import de.luuuke.pcubes.models.ValueGroupCategorical;
import de.luuuke.pcubes.models.ValueGroupDate;
import de.luuuke.pcubes.models.ValueGroupNumber;
import de.luuuke.pcubes.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/dimension_attribute")
public class DimensionAttributeController extends BasicController<DimensionAttribute> {

  private ValueGroupNumberRepository valueGroupNumberRepository;
  private ValueGroupCategoricalRepository valueGroupCategoricalRepository;
  private ValueGroupDateRepository valueGroupDateRepository;
  private ValueGroupRepository valueGroupRepository;
  private DimensionAttributeRepository dimensionAttributeRepository;
  private DimensionElementValueRepository dimensionElementValueRepository;

  @Autowired
  public DimensionAttributeController(
      ValueGroupNumberRepository valueGroupNumberRepository,
      ValueGroupCategoricalRepository valueGroupCategoricalRepository,
      ValueGroupDateRepository valueGroupDateRepository,
      ValueGroupRepository valueGroupRepository,
      DimensionAttributeRepository dimensionAttributeRepository,
      CrudRepository<DimensionAttribute, Long> repository, DimensionElementValueRepository dimensionElementValueRepository) {
    super(repository);

    this.valueGroupNumberRepository = valueGroupNumberRepository;
    this.valueGroupCategoricalRepository = valueGroupCategoricalRepository;
    this.valueGroupDateRepository = valueGroupDateRepository;
    this.valueGroupRepository = valueGroupRepository;
    this.dimensionAttributeRepository = dimensionAttributeRepository;

    this.dimensionElementValueRepository = dimensionElementValueRepository;
  }

  @GetMapping("")
  @Override
  public ResponseEntity<Iterable<DimensionAttribute>> getAll() {
    return ResponseEntity.ok(dimensionAttributeRepository.findAllByOrderByCreatedAtAsc());
  }

  @DeleteMapping("{id}")
  @Override
  public ResponseEntity<DimensionAttribute> delete(@PathVariable Long id) {
    Optional<DimensionAttribute> attribute = dimensionAttributeRepository.findById(id);
    if (attribute.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Iterable<DimensionElementValue> byDimensionAttribute = dimensionElementValueRepository.findAllByDimensionAttribute(attribute.get());
    dimensionElementValueRepository.deleteAll(byDimensionAttribute);

    dimensionAttributeRepository.delete(attribute.get());

    return ResponseEntity.ok().build();
  }



  @PostMapping("{attrId}/vgroup_number")
  public ResponseEntity<ValueGroupNumber> addVGroupNumber(@PathVariable Long attrId,
                                                          @RequestBody ValueGroupNumber valueGroupNumber) {
    Optional<DimensionAttribute> attribute = dimensionAttributeRepository.findById(attrId);
    if (attribute.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    valueGroupNumber = new ValueGroupNumber(attribute.get(), valueGroupNumber.getLower(), valueGroupNumber.getUpper());
    ValueGroupNumber saved = valueGroupNumberRepository.save(valueGroupNumber);
    return ResponseEntity.ok(saved);
  }

  @PostMapping("{attrId}/vgroup_categorical")
  public ResponseEntity<ValueGroupCategorical> addVGroupCategorical(@PathVariable Long attrId,
                                                                    @RequestBody ValueGroupCategorical valueGroupCategorical) {
    Optional<DimensionAttribute> attribute = dimensionAttributeRepository.findById(attrId);
    if (attribute.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    valueGroupCategorical = new ValueGroupCategorical(attribute.get(), valueGroupCategorical.getValues());
    ValueGroupCategorical saved = valueGroupCategoricalRepository.save(valueGroupCategorical);
    return ResponseEntity.ok(saved);
  }

  @PostMapping("{attrId}/vgroup_date")
  public ResponseEntity<ValueGroupDate> addVGroupDate(@PathVariable Long attrId,
                                                      @RequestBody ValueGroupDate valueGroupDate) {
    Optional<DimensionAttribute> attribute = dimensionAttributeRepository.findById(attrId);
    if (attribute.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    valueGroupDate = new ValueGroupDate(attribute.get(), valueGroupDate.getStartDate(), valueGroupDate.getEndDate());
    ValueGroupDate saved = valueGroupDateRepository.save(valueGroupDate);
    return ResponseEntity.ok(saved);
  }
}

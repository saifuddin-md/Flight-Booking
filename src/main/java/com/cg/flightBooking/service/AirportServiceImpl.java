package com.cg.flightBooking.service;

import com.cg.flightBooking.exception.RecordAlreadyExistException;
import com.cg.flightBooking.exception.RecordNotFoundException;
import com.cg.flightBooking.model.Airport;
import com.cg.flightBooking.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AirportServiceImpl implements AirportService {
	@Autowired
	AirportRepository airportRepository;

	/*
	 * view all Airports
	 */
	@Override
	public Iterable<Airport> viewAllAirport() {
		return airportRepository.findAll();
	}

	/*
	 * view airport by airportCode
	 */
	@Override
	public Airport viewAirport(String airportCode) {
		Optional<Airport> findById = airportRepository.findById(airportCode);
		if (findById.isPresent()) {
			return findById.get();
		}

		else
			throw new RecordNotFoundException("Airport with airport code: " + airportCode + "not exists");
	}


	/*
	 * add a airport
	 */
	@Override
	public ResponseEntity<?> addAirport(Airport airport) {
		Optional<Airport> findById = airportRepository.findById(airport.getAirportCode());
		try {
		if (!findById.isPresent()) {
			airportRepository.save(airport);
			return new ResponseEntity<Airport>(airport,HttpStatus.OK);
		} 
		else
			throw new RecordAlreadyExistException(
					"Airport with code : " + airport.getAirportCode() + " already present");
	     }
		catch(RecordAlreadyExistException e)
		{
			return new ResponseEntity<Airport>(airport,HttpStatus.NOT_FOUND);
		}
	}

	/*
	 * modify an Airport
	 */
	@Override
	public Airport modifyAirport(Airport airport) {
		Optional<Airport> findById = airportRepository.findById(airport.getAirportCode());
		if (findById.isPresent()) {
			airportRepository.save(airport);
		} 
		else
			throw new RecordNotFoundException("Airport with code: " + airport.getAirportCode() + " not exists");
		return airport;
	}

	/*
	 * remove an airport
	 */
	@Override
	public String removeAirport(String airportCode) {
		Optional<Airport> findById = airportRepository.findById(airportCode);
		if (findById.isPresent()) {
			airportRepository.deleteById(airportCode);
			return "Airport removed";
		} else
			throw new RecordNotFoundException("Airport with code: " + airportCode + " not exists");

	}
}

package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ISA2020.farmacia.entity.basic.Drug;
import com.ISA2020.farmacia.entity.basic.Farmacy;
import com.ISA2020.farmacia.entity.basic.Views;
import com.ISA2020.farmacia.entity.users.FarmacyAdmin;
import com.ISA2020.farmacia.repository.DrugRepository;
import com.ISA2020.farmacia.repository.FarmacyAdminRepository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.security.JwtUtils;
import com.ISA2020.farmacia.util.GeoUtils;
import com.fasterxml.jackson.annotation.JsonView;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/farmacy")
public class FarmacyController {

	@Autowired
	FarmacyRepository farmacyRepo;
	@Autowired
	DrugRepository drugRepo;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	GeoUtils geoUtils;
	@Autowired
	FarmacyAdminRepository farmAdminRepo;
	final String accessKey = "6c93357a36b238e6e01c463a206f8e92";
	
	@GetMapping(value ="/getLongLat/{farmacyId}", produces = { "application/json" })
	public ResponseEntity<?> longlat(@PathVariable Long farmacyId) throws URISyntaxException, JSONException {	
		RestTemplate restTemplate = new RestTemplate();
		String adress = geoUtils.getQueryAdress(farmacyRepo.getById(String.valueOf(farmacyId)).getAdress());
		final String baseUrl = "http://api.positionstack.com/v1/forward"
				+ "?access_key=" + accessKey + "&query=" + adress;
		URI uri = new URI(baseUrl);
		String result = restTemplate.getForObject(uri, String.class);
		JSONObject o =   geoUtils.getLatLong(result);	
		return new ResponseEntity<>(o, HttpStatus.OK);
	}
	
	@JsonView(Views.SimpleFarmacy.class)
	@GetMapping("/profile")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public Farmacy myfarmProfile(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		return  farmAdminRepo.findById(username).get().getFarmacy();
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token,@Valid @RequestBody Farmacy farmacy) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		FarmacyAdmin admin = farmAdminRepo.findById(username).get();
		if(!admin.getFarmacy().getId().equals(farmacy.getId())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		farmacy.setStars(farmacyRepo.findById(admin.getFarmacy().getId()).get().getStars());
		farmacyRepo.save(farmacy);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@JsonView(Views.SimpleDrug.class)
	@GetMapping("/drugs")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public List<Drug> farmacyDrugs(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		return farmAdminRepo.findById(username).get().getFarmacy().getDrugs();
		 
	}
	
	
	@JsonView(Views.SimpleDrug.class)
	@GetMapping("/search/{parametar}")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<Object> searchFarmacyDrugs(@RequestHeader("Authorization") String token,@PathVariable String parametar) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		 StringBuilder sb = new StringBuilder(parametar.concat("%"));
		 sb.insert(0,"%");
		 List<Drug> drugs = drugRepo.findByNameLikeIgnoreCaseOrCodeLikeIgnoreCase(sb.toString(),sb.toString());
		if(drugs.isEmpty() || drugs==null) {
			return ResponseEntity.notFound().build();
		}
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy = farmAdminRepo.findById(username).get().getFarmacy();
		
		drugs.removeIf(drug -> !drug.getFarmacies().contains(farmacy));
		return new ResponseEntity<Object>(drugs, HttpStatus.OK);
		
		
		
	
		 
		 
	}
	
	@PostMapping("/drug/{code}")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> farmacyDrugs(@RequestHeader("Authorization") String token, @PathVariable String code) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		Optional<Drug> drug = drugRepo.findById(code);
		if(drug.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		boolean check = drug.get().addFarmacy(farmacy);
		if(!check) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		drugRepo.save(drug.get());
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	

	@DeleteMapping("/drug/{code}")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> farmacyDrug(@RequestHeader("Authorization") String token, @PathVariable String code) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		Optional<Drug> drug = drugRepo.findById(code);
		if(drug.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		boolean check = drug.get().deleteFarmacy(farmacy);
		if(!check) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		drugRepo.save(drug.get());
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
	
	@JsonView(Views.SimpleFarmacy.class)
	@GetMapping("/profile/{id}")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> farmProfile( @PathVariable String id)  {	
		Optional<Farmacy> farmacy =   farmacyRepo.findById(id);
		if(farmacy.isEmpty()) return ResponseEntity.notFound().build();
		return new ResponseEntity<>(farmacy.get(), HttpStatus.OK);
		
	}
	
	
	@JsonView(Views.SimpleDrug.class)
	@GetMapping("/drugsAvailable/{id}")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> farmacyAvailableDrugs(@PathVariable String id){	
		Farmacy farmacy = farmacyRepo.getById(id);
		List<Drug> drugs = new ArrayList<>();
		farmacy.getDrugsQuantities().forEach((k, v) -> drugs.add(k));
		if(drugs.isEmpty()) return ResponseEntity.notFound().build();
		return new ResponseEntity<>(drugs, HttpStatus.OK);
	}
	
	@JsonView(Views.SimpleFarmacy.class)
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> getAllFarmacies(){	
		List<Farmacy> farmacies = farmacyRepo.findAll();
		return new ResponseEntity<>(farmacies, HttpStatus.OK);
	}
	
	

}

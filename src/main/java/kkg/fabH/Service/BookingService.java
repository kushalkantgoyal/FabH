/*package kkg.fabH.Service;

import java.util.Date;

import kkg.fabH.DAO.BookingDAO;
import kkg.fabH.Entity.BookingEntity;
import kkg.fabH.Entity.TaxiEntity;
import kkg.fabH.Entity.UserEntity;
import kkg.fabH.Util.TaxiType;
import kkg.fabH.Util.Utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

*//**
 * @author Kushal
 *
 *//*
@Service("bookingService")
@Transactional
public class BookingService {
	
	@Autowired
	private BookingDAO bookingDAO;
	
	@Autowired
	private TaxiService taxiService;
	
	private static final Logger logger = Logger.getLogger(BookingService.class);
	
	public synchronized BookingEntity bookTaxi(Long userId, TaxiType taxiType, Double userLat, Double userLon) throws Exception{
		TaxiEntity taxi = null;
		try{
				taxi = taxiService.findNearestTaxi(taxiType, userLat, userLon);
		}catch(Exception e){
			logger.error("Exception while finding nearest taxi for userId: "+userId, e);
			throw new Exception("Exception while finding nearest taxi for userId: "+userId, e);
		}
		if(taxi!=null){
			taxi.setOccupied(true);
			taxi = taxiService.updateTaxi(taxi);
			if(taxi!=null){
				BookingEntity booking = new BookingEntity();
				UserEntity user = new UserEntity();
				user.setId(userId);
				booking.setUser(user);
				booking.setTaxi(taxi);
				booking.setStartLat(userLat);
				booking.setStartLon(userLon);
				BookingEntity newBooking = createBooking(booking);
				return newBooking;
			}
			return null;
		}else{
			return null;
		}
	}
	
	public BookingEntity getBookingById(Long id) throws Exception {
		try{
			return bookingDAO.getById(id);
		}catch(Exception e){
			logger.error("Exception while fetching booking for id: "+id, e);
			throw new Exception("Exception while fetching booking for id: "+id, e);
		}
	}

	public BookingEntity createBooking(BookingEntity booking) throws Exception{
		try{
			bookingDAO.create(booking);
			return booking;
		}catch(Exception e){
			logger.error("Exception while creating booking: "+booking, e);
			throw new Exception("Exception while creating booking: "+booking, e);
		}
	}
	
	public BookingEntity updateBooking(BookingEntity newBooking) throws Exception{
		try{
			BookingEntity oldBooking = bookingDAO.getById(newBooking.getId());
			
			if(newBooking.getUser()!=null){
				UserEntity user = new UserEntity();
				user.setId(newBooking.getUser().getId());
				newBooking.setUser(user);
			}
			if(newBooking.getTaxi()!=null){
				TaxiEntity taxi = new TaxiEntity();
				taxi.setId(newBooking.getTaxi().getId());
				newBooking.setTaxi(taxi);
			}
			if(newBooking.getEndTime()!=null){
				oldBooking.setEndTime(newBooking.getEndTime());
			}
			if(newBooking.getStartTime()!=null){
				oldBooking.setStartTime(newBooking.getStartTime());
			}
			if(newBooking.getStartLat()!=null){
				oldBooking.setStartLat(newBooking.getStartLat());
			}
			if(newBooking.getStartLon()!=null){
				oldBooking.setStartLon(newBooking.getStartLon());
			}
			if(newBooking.getEndLat()!=null){
				oldBooking.setEndLat(newBooking.getEndLat());
			}
			if(newBooking.getEndLon()!=null){
				oldBooking.setEndLon(newBooking.getEndLon());
			}
			bookingDAO.update(oldBooking);
			return oldBooking;
			
		}catch(Exception e){
			logger.error("Exception while updating booking: "+newBooking, e);
			throw new Exception("Exception while updating booking: "+newBooking, e);
		}
	}
	
	public BookingEntity startRide(Long bookingId) throws Exception{
		try{
			BookingEntity booking = bookingDAO.getById(bookingId);
			booking.setStartTime(new Date());
			
			bookingDAO.update(booking);
			return booking;
			
		}catch(Exception e){
			logger.error("Exception while starting the ride for bookingId: "+bookingId, e);
			throw new Exception("Exception while starting the ride for bookingId: "+bookingId, e);
		}
	}
	
	public BookingEntity endRide(Long bookingId, Double endLat, Double endLon) throws Exception{
		try{
			BookingEntity booking = bookingDAO.getById(bookingId);
			booking.setEndTime(new Date());
			booking.setEndLat(endLat);
			booking.setEndLon(endLon);
			booking = calculateFare(booking);
			bookingDAO.update(booking);
			TaxiEntity taxi = booking.getTaxi();
			taxi.setLat(endLat);
			taxi.setLon(endLon);
			taxi.setOccupied(false);
			taxiService.updateTaxi(taxi);
			return booking;
			
		}catch(Exception e){
			logger.error("Exception while ending the ride for bookingId: "+bookingId, e);
			throw new Exception("Exception while ending the ride for bookingId: "+bookingId, e);
		}
	}
	
	private BookingEntity calculateFare(BookingEntity booking) throws Exception{
		try{
			Float fare = (float) (booking.getTaxi().getTaxiType().getBaseFare() +
			booking.getTaxi().getTaxiType().getRatePerKm() * calculateDistance(booking) +
			booking.getTaxi().getTaxiType().getRatePerMin() * calculateRideTime(booking));
			
			booking.setFare(fare);
			return booking;
			
		}catch(Exception e){
			logger.error("Exception while calculating the fare for bookingId: "+booking.getId(), e);
			throw new Exception("Exception while calculating the fare for bookingId: "+booking.getId(), e);
		}
	}
	
	private Double calculateDistance(BookingEntity booking){
		double distance = Math.pow((Math.pow((booking.getEndLat() - booking.getStartLat()), 2)
		 + Math.pow((booking.getEndLon() - booking.getStartLon()),2)), 0.5);
		return Utils.round(distance, 2);
	}
	
	private Long calculateRideTime(BookingEntity booking){
		Long minutes = (booking.getEndTime().getTime() - booking.getStartTime().getTime())/60000;
		return minutes;
	}

	public Boolean removeBooking(Long bookingId) throws Exception {
		BookingEntity booking = null;
		try{
			booking = bookingDAO.getById(bookingId);
		}catch(Exception e){
			logger.error("Exception while fetching booking with id: "+bookingId, e);
			throw new Exception("Exception while fetching booking with id: "+bookingId, e);
		}
		if(booking!=null){
			try{
				return bookingDAO.remove(booking);
			}catch(Exception e){
				logger.error("Exception while removing booking with id: "+bookingId, e);
				return false;
			}
		}
		else{
			return false;
		}
	}
}
*/
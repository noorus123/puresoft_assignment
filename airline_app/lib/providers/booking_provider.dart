import 'package:flutter/material.dart';
import '../models/booking.dart';
import '../services/api_service.dart';

class BookingProvider extends ChangeNotifier {
  final ApiService _api = ApiService();

  Booking? booking;
  bool loading = false;
  String? error;

  Future<void> createBooking(
      int flightId, String seatNumber, String passengerName) async {
    loading = true;
    error = null;
    notifyListeners();

    try {
      booking = await _api.createBooking(
          flightId, seatNumber, passengerName);
    } catch (e) {
      error = e.toString();
    }

    loading = false;
    notifyListeners();
  }

  Future<void> refreshBooking(int id) async {
    booking = await _api.getBooking(id);
    notifyListeners();
  }
}

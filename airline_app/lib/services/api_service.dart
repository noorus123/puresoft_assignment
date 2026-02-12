import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/seat.dart';
import '../models/booking.dart';

class ApiService {
  //static const String baseUrl = "http://192.168.1.7:8080"; #Android
  static const String baseUrl = "http://localhost:8080";


  Future<List<Seat>> fetchSeats(int flightId) async {
    final response =
    await http.get(Uri.parse('$baseUrl/api/seats/$flightId'));

    if (response.statusCode == 200) {
      List data = jsonDecode(response.body);
      return data.map((e) => Seat.fromJson(e)).toList();
    } else {
      throw Exception("Failed to load seats");
    }
  }

  Future<Booking> createBooking(
      int flightId, String seatNumber, String passengerName) async {
    final response = await http.post(
      Uri.parse('$baseUrl/api/bookings'),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({
        "flightId": flightId,
        "seatNumber": seatNumber,
        "passengerName": passengerName,
      }),
    );

    if (response.statusCode == 200) {
      return Booking.fromJson(jsonDecode(response.body));
    } else {
      final error = jsonDecode(response.body);
      throw Exception(error["error"]);
    }
  }

  Future<Booking> getBooking(int id) async {
    final response =
    await http.get(Uri.parse('$baseUrl/api/bookings/$id'));

    if (response.statusCode == 200) {
      return Booking.fromJson(jsonDecode(response.body));
    } else {
      throw Exception("Booking not found");
    }
  }

  Future<List<dynamic>> fetchFlights() async {
    final response =
    await http.get(Uri.parse('$baseUrl/api/flights'));

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception("Failed to load flights");
    }
  }

}

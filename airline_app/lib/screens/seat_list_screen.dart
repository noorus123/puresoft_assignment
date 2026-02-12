import 'package:flutter/material.dart';
import '../models/seat.dart';
import '../services/api_service.dart';
import 'booking_screen.dart';

class SeatListScreen extends StatefulWidget {
  final int flightId;

  const SeatListScreen({
    super.key,
    required this.flightId,
  });

  @override
  State<SeatListScreen> createState() => _SeatListScreenState();
}

class _SeatListScreenState extends State<SeatListScreen> {
  final ApiService api = ApiService();
  late Future<List<Seat>> seatsFuture;

  @override
  void initState() {
    super.initState();
    seatsFuture = api.fetchSeats(widget.flightId);
  }

  Color statusColor(String status) {
    switch (status) {
      case "AVAILABLE":
        return Colors.green;
      case "LOCKED":
        return Colors.orange;
      case "BOOKED":
        return Colors.red;
      default:
        return Colors.grey;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Seat List"),
        centerTitle: true,
      ),
      body: FutureBuilder<List<Seat>>(
        future: seatsFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return Center(
              child: Text(
                "Error: ${snapshot.error}",
                style: const TextStyle(color: Colors.red),
              ),
            );
          }

          final seats = snapshot.data!;

          return ListView.builder(
            itemCount: seats.length,
            itemBuilder: (context, index) {
              final seat = seats[index];

              return Card(
                margin:
                const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                elevation: 3,
                child: ListTile(
                  title: Text(
                    "Seat ${seat.seatNumber}",
                    style: const TextStyle(
                        fontWeight: FontWeight.bold),
                  ),
                  subtitle: Text(
                    "Status: ${seat.status}",
                    style: TextStyle(
                        color: statusColor(seat.status)),
                  ),
                  trailing: seat.status == "AVAILABLE"
                      ? const Icon(Icons.flight_takeoff)
                      : null,
                  enabled: seat.status == "AVAILABLE",
                  onTap: seat.status == "AVAILABLE"
                      ? () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => BookingScreen(
                          flightId: seat.flightId,
                          seatNumber: seat.seatNumber,
                        ),
                      ),
                    );
                  }
                      : null,
                ),
              );
            },
          );
        },
      ),
    );
  }
}

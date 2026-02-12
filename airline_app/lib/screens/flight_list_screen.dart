import 'package:flutter/material.dart';
import '../services/api_service.dart';
import 'seat_list_screen.dart';

class FlightListScreen extends StatefulWidget {
  const FlightListScreen({super.key});

  @override
  State<FlightListScreen> createState() =>
      _FlightListScreenState();
}

class _FlightListScreenState
    extends State<FlightListScreen> {
  final ApiService api = ApiService();
  late Future<List<dynamic>> flightsFuture;

  @override
  void initState() {
    super.initState();
    flightsFuture = api.fetchFlights();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Available Flights"),
        centerTitle: true,
      ),
      body: FutureBuilder<List<dynamic>>(
        future: flightsFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState ==
              ConnectionState.waiting) {
            return const Center(
                child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return Center(
                child: Text(
                    "Error: ${snapshot.error}"));
          }

          final flights = snapshot.data!;

          return ListView.builder(
            itemCount: flights.length,
            itemBuilder: (context, index) {
              final flight = flights[index];

              return Card(
                margin:
                    const EdgeInsets.symmetric(
                        horizontal: 12,
                        vertical: 6),
                child: ListTile(
                  title: Text(
                    "Flight ${flight['flightNumber']}",
                    style: const TextStyle(
                        fontWeight:
                            FontWeight.bold),
                  ),
                  subtitle: Text(
                      "${flight['source']} → ${flight['destination']}"),
                  trailing:
                      const Icon(Icons.arrow_forward),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) =>
                            SeatListScreen(
                          flightId:
                              flight['id'],
                        ),
                      ),
                    );
                  },
                ),
              );
            },
          );
        },
      ),
    );
  }
}

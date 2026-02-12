import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/booking_provider.dart';
import 'booking_status_screen.dart';

class BookingScreen extends StatefulWidget {
  final int flightId;
  final String seatNumber;

  const BookingScreen({
    super.key,
    required this.flightId,
    required this.seatNumber,
  });

  @override
  State<BookingScreen> createState() => _BookingScreenState();
}

class _BookingScreenState extends State<BookingScreen> {
  final TextEditingController nameController =
  TextEditingController();

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<BookingProvider>();

    return Scaffold(
      appBar: AppBar(
        title: const Text("Create Booking"),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              "Seat: ${widget.seatNumber}",
              style: const TextStyle(
                  fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: nameController,
              decoration: const InputDecoration(
                labelText: "Passenger Name",
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(
                      vertical: 14),
                ),
                onPressed: provider.loading
                    ? null
                    : () async {
                  if (nameController.text.isEmpty) {
                    ScaffoldMessenger.of(context)
                        .showSnackBar(
                      const SnackBar(
                        content: Text(
                            "Enter passenger name"),
                      ),
                    );
                    return;
                  }

                  await provider.createBooking(
                    widget.flightId,
                    widget.seatNumber,
                    nameController.text,
                  );

                  if (provider.booking != null) {
                    Navigator.pushReplacement(
                      context,
                      MaterialPageRoute(
                        builder: (_) =>
                        const BookingStatusScreen(),
                      ),
                    );
                  }
                },
                child: provider.loading
                    ? const CircularProgressIndicator(
                  color: Colors.white,
                )
                    : const Text("Book Now"),
              ),
            ),
            if (provider.error != null)
              Padding(
                padding:
                const EdgeInsets.only(top: 12),
                child: Text(
                  provider.error!,
                  style: const TextStyle(
                      color: Colors.red),
                ),
              )
          ],
        ),
      ),
    );
  }
}

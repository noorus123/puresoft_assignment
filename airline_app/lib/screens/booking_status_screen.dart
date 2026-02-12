import 'dart:async';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/booking_provider.dart';

class BookingStatusScreen extends StatefulWidget {
  const BookingStatusScreen({super.key});

  @override
  State<BookingStatusScreen> createState() =>
      _BookingStatusScreenState();
}

class _BookingStatusScreenState
    extends State<BookingStatusScreen> {
  Timer? timer;

  @override
  void initState() {
    super.initState();

    final provider =
    Provider.of<BookingProvider>(context, listen: false);

    timer = Timer.periodic(
      const Duration(seconds: 2),
          (_) {
        if (provider.booking != null &&
            provider.booking!.status ==
                "PENDING_PAYMENT") {
          provider.refreshBooking(
              provider.booking!.bookingId);
        } else {
          timer?.cancel();
        }
      },
    );
  }

  @override
  void dispose() {
    timer?.cancel();
    super.dispose();
  }

  Color statusColor(String status) {
    switch (status) {
      case "CONFIRMED":
        return Colors.green;
      case "FAILED":
        return Colors.red;
      case "PENDING_PAYMENT":
        return Colors.orange;
      default:
        return Colors.grey;
    }
  }

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<BookingProvider>();
    final booking = provider.booking;

    return Scaffold(
      appBar: AppBar(
        title: const Text("Booking Status"),
        centerTitle: true,
      ),
      body: Center(
        child: booking == null
            ? const Text("No booking found")
            : Column(
          mainAxisAlignment:
          MainAxisAlignment.center,
          children: [
            Text(
              "Booking ID: ${booking.bookingId}",
              style: const TextStyle(
                  fontSize: 18),
            ),
            const SizedBox(height: 10),
            Text("Seat: ${booking.seatNumber}"),
            Text(
                "Passenger: ${booking.passengerName}"),
            const SizedBox(height: 20),
            Text(
              booking.status,
              style: TextStyle(
                fontSize: 22,
                fontWeight:
                FontWeight.bold,
                color:
                statusColor(booking.status),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

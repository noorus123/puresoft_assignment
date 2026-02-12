class Booking {
  final int bookingId;
  final int flightId;
  final String seatNumber;
  final String passengerName;
  final String status;

  Booking({
    required this.bookingId,
    required this.flightId,
    required this.seatNumber,
    required this.passengerName,
    required this.status,
  });

  factory Booking.fromJson(Map<String, dynamic> json) {
    return Booking(
      bookingId: json['bookingId'],
      flightId: json['flightId'],
      seatNumber: json['seatNumber'],
      passengerName: json['passengerName'],
      status: json['status'],
    );
  }
}

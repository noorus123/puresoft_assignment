class Seat {
  final int id;
  final int flightId;
  final String seatNumber;
  final String status;

  Seat({
    required this.id,
    required this.flightId,
    required this.seatNumber,
    required this.status,
  });

  factory Seat.fromJson(Map<String, dynamic> json) {
    return Seat(
      id: json['id'],
      flightId: json['flightId'],
      seatNumber: json['seatNumber'],
      status: json['status'],
    );
  }
}

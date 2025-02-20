import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';

class TripDetailScreen extends StatelessWidget {
  final String tripId;

  TripDetailScreen({required this.tripId});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Détails du Trajet')),
      body: FutureBuilder(
        future: FirebaseFirestore.instance.collection('trips').doc(tripId).get(),
        builder: (context, AsyncSnapshot<DocumentSnapshot> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          }
          if (!snapshot.hasData) {
            return Center(child: Text('Trajet non trouvé'));
          }

          var trip = snapshot.data!;
          return Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('Origine: ${trip['origin']}', style: TextStyle(fontSize: 18)),
                Text('Destination: ${trip['destination']}', style: TextStyle(fontSize: 18)),
                Text('Prix: \$${trip['price']}', style: TextStyle(fontSize: 18)),
                Text('Conducteur: ${trip['driver']}', style: TextStyle(fontSize: 18)),
                SizedBox(height: 20),
                ElevatedButton(
                  onPressed: () {
                    // Logique de réservation ou autres actions
                  },
                  child: Text('Réserver'),
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}

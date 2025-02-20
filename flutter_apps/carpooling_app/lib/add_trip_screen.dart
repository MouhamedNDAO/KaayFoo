import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';

class AddTripScreen extends StatefulWidget {
  @override
  _AddTripScreenState createState() => _AddTripScreenState();
}

class _AddTripScreenState extends State<AddTripScreen> {
  final TextEditingController _originController = TextEditingController();
  final TextEditingController _destinationController = TextEditingController();
  final TextEditingController _priceController = TextEditingController();

  Future<void> _addTrip() async {
    try {
      await FirebaseFirestore.instance.collection('trips').add({
        'origin': _originController.text,
        'destination': _destinationController.text,
        'price': double.tryParse(_priceController.text) ?? 0,
        'driver': 'User Name', // Utilise le nom de l'utilisateur connecté
        'dateTime': DateTime.now(),
      });
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Trajet ajouté")));
      Navigator.pop(context);
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Erreur : ${e.toString()}")));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Ajouter un Trajet')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _originController,
              decoration: InputDecoration(labelText: 'Origine'),
            ),
            TextField(
              controller: _destinationController,
              decoration: InputDecoration(labelText: 'Destination'),
            ),
            TextField(
              controller: _priceController,
              keyboardType: TextInputType.number,
              decoration: InputDecoration(labelText: 'Prix'),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: _addTrip,
              child: Text('Publier'),
            ),
          ],
        ),
      ),
    );
  }
}

import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'auth_screen.dart'; // Écran de connexion/utilisateur
import 'home_screen.dart'; // Écran d'accueil des trajets

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Co-voiturage Local',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: AuthScreen(), // Page de connexion
    );
  }
}


//Ajouter si possible la redirection automatique vers la page d'accueil si celui-ci c'etait dejas connecter dans l'app

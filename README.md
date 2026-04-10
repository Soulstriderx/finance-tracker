<div align="center">

# 🚀 Finance Tracker (Android)

A modern Android finance management app built with Kotlin, Jetpack Compose, and Firebase.

</div>

---

## 📌 Overview

Finance Tracker is a mobile application designed to help users manage personal finances by tracking income, expenses, budgets, and bills in real time.

It provides a simple, structured, and cloud-synced overview of spending habits.

---

## ✨ Key Features

- 💰 Add income and expense transactions  
- 📊 Real-time balance calculation  
- 🧾 Monthly budget tracking with rollover support  
- 📅 Bill tracking with due dates  
- ☁️ Cloud sync using Firebase Firestore  
- 🔐 Firebase Authentication support  
- 📱 Jetpack Compose UI  
- 🗂 Transaction history management  

---

## 🏗️ Tech Stack

- Platform: Android (Android Studio)  
- Language: Kotlin  
- UI: Jetpack Compose (Material 3)  
- Backend: Firebase Firestore  
- Auth: Firebase Authentication  

---

## ⚙️ Architecture

- UI Layer: Jetpack Compose screens  
- ViewModel Layer: State + logic handling  
- Repository Layer: Firebase operations  
- Backend: Firebase Firestore  

---

## ⚙️ Setup & Installation

1. Clone the repository:

    `git clone https://github.com/Soulstriderx/finance-tracker.git`

2. Open in Android Studio

3. Configure Firebase:
    - Create Firebase project
    - Register Android app
    - Enable Firestore + Auth
    - Download google-services.json
    - Place it inside /app

4. Sync Gradle

5. Run on emulator or device

---

## 📊 How It Works

- User registers and logs in via Firebase Auth  
- Transactions stored in Firestore under user account  
- App listens to real-time updates  
- UI updates automatically using Compose state  
- Budget and bills update dynamically using the same process  

---

## 🎯 What This Project Demonstrates

- Modern Android development with Jetpack Compose  
- Firebase integration (Firestore + Auth)  
- Real-time data synchronization  
- Clean architecture (MVVM-style)  
- Reactive UI design  

---

## 📷 Screenshots

Screenshots TBA (dashboard, transactions, budgets, bills)

---

## 🚀 Future Improvements

- Export data as PDF/CSV  
- Dark mode support  
- Notification reminders for bills  
- Advanced analytics dashboard  

---

## 👤 Author

Alex Sim  
GitHub: https://github.com/Soulstriderx

---

## 📄 License

This project is open-source under the MIT License.

# OpenAir - Social Media for Positive Behavioral Change

## Overview
OpenAir is a prototype mobile social media application designed to **encourage** 
users to **spend more time outdoors**.

By combining social engagement with gamification,
the app aims to promote healthier lifestyles while mitigating the negative effects 
associated with traditional social media platforms.

To determine the effectiveness of this concept and design, the app was
released in a closed alpha study during a 2 week study which measured 
time spent outdoors before and after exposure to the application.

## Purpose
Modern society often struggles with the health impacts of indoor lifestyles,
such as fatigue and reduced cognitive and mental well-being. 
OpenAir seeks to counter this trend by leveraging social motivation 
and a simple reward system to inspire outdoor activities.

## Core Features
### Activity Posting

- Users can share outdoor activities by uploading photos, describing the activity, 
  and categorizing it into predefined types such as hiking, swimming, or general outdoor 
  activities.

- The posts are displayed in a visually appealing, scrollable feed that resembles a
  timeline of outdoor adventures.

### Scrollable Feed

- The home feed displays all users' posts, simulating a “friends” list
  for the prototype phase.
  
- Posts are presented in a minimalist design with an emphasis on images
  and activity descriptions to inspire viewers without causing overstimulation
  or app dependency.

### Gamification with Badge System

- To encourage consistent outdoor engagement, users unlock badges by
  reaching specific milestones, such as completing a set number of activities.
  
- Different badge types are linked to specific activity categories, offering
  variety and catering to diverse interests.
  
### My Stuff Section

- A personalized space where users can view their own posts and monitor
  their progress toward badge achievements.
  
- This section includes a clean and simple badge display that motivates
  users to engage more frequently in outdoor activities.
  
### Streamlined User Interface

- OpenAir’s design prioritizes minimalism to reduce potential distractions
  and keep users focused on outdoor activity rather than prolonged app usage.

## Technical Implementation

### Platform and Development Tools

- The app was developed for Android using Java in Android Studio.
- Firebase services power the backend, providing authentication,
  data storage, and real-time synchronization.
  
### Firebase Integration

- Authentication: Secure login and signup processes using email-password pairs.
  
- Cloud Firestore: A NoSQL database to store user data, including posts, profiles,
  and activity statistics.
  
- Firebase Storage: Used for handling user-uploaded images, such as profile pictures
  and activity photos.
  
### Frontend Development

- RecyclerView and CardView: Implemented to create dynamic, scrollable feeds that
  display user posts in a card-like format.
  
- XML Layouts: Used for designing screens like login, signup, home feed,
  and profile management.
  
- Minimalistic Styling: Inspired by popular apps like Instagram and BeReal,
  but adapted to discourage app addiction.
  
### Backend Architecture

- A singleton design pattern is used to manage user and post data across
  sessions efficiently.
  
- Data retrieval and storage are optimized by separating user and post information
  into distinct Firestore collections, allowing for seamless data operations.
  
### Badge System Logic

- Badges are awarded based on activity thresholds stored in the Firestore database.
  
- Each activity type increments a counter in the user’s document, which is checked
  to determine badge unlock conditions.
  
## Study and Results
The application underwent a closed alpha test with a small group of participants 
to evaluate its impact on encouraging outdoor activities. Participants provided 
quantitative data on their outdoor activities before and after using OpenAir, 
along with qualitative feedback on the app's design, usability, and motivational effects.

Positive Outcomes:
Some users reported increased motivation to perform outdoor activities, 
particularly driven by the badge system and the social encouragement from 
seeing others’ posts.

Challenges Identified:
Environmental factors like weather and location changes significantly 
impacted activity levels, highlighting areas for improvement in the study 
design and app features.

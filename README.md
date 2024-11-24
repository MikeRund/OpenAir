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
OpenAir seeks to showcase that this trend can be countered by leveraging  the modern 
technologies, and harnessing the power of social motivation and a simple reward system 
in a social media application to inspire users to perform more outdoor activities.

## Demo
This demo will go through a use case where a pre-existing user: 

1. Logs into their account
2. Views the homepage, populated with their own and fellow users posts
3. Opens the edit profile section
4. Moves to the "My Profile" section, where they can view their unlocked
   badges and their own posted activites
5. Creates a new post activity, and see it in the home and personal feed
6. Notice the left-most badge is now unlocked

https://github.com/user-attachments/assets/3080d697-1ca3-4d06-85e1-63e3649a1a2a


## Core Features
The funcitonality of this application is based around users posting their
outdoor activities, which based off catgeories and an unlock system earns
them corresponding badges, as well as exposure to their friends posts via a 
scrollable feed.


### Activity Posting

- Users can share outdoor activities by uploading photos, describing the activity, 
  and categorizing it into predefined types such as hiking, swimming, or general outdoor 
  activities.

- The posts are displayed in a visually appealing, scrollable feed that resembles a
  timeline of outdoor adventures.

  ![image](https://github.com/user-attachments/assets/3c0f224c-19b4-48ff-8127-e3bcc8a22e04)

### Scrollable Feed

- The home feed displays all users' posts, simulating a “friends” list
  for the prototype phase.
  
- Posts are presented in a minimalist design with an emphasis on images
  and activity descriptions to inspire viewers without causing overstimulation
  or app dependency.

![image](https://github.com/user-attachments/assets/6ed621f4-4337-4b27-8638-7413e1942041) ![image](https://github.com/user-attachments/assets/dd3d30a7-d3ae-4031-b891-c7016c4afddb)


### My Stuff Section and Badge System

- A personalized space where users can view their own posts and monitor
  their progress toward badge achievements.
  
- This section includes a clean and simple badge display that motivates
  users to engage more frequently in outdoor activities.

- To encourage consistent outdoor engagement, users unlock badges by
  reaching specific milestones, such as completing a set number of activities.
  
- Different badge types are linked to specific activity categories, offering
  variety and catering to diverse interests.

![image](https://github.com/user-attachments/assets/eea14b93-cc61-44b2-9004-2a109159dcdc) ![image](https://github.com/user-attachments/assets/b638062a-cb6d-44e8-8b57-8a19c4a93564)

### Edit Profile Section

- Users can select an image to display as a profile picture from their
  camera role and and a short description for themselves

  ![image](https://github.com/user-attachments/assets/bed4440e-2ba6-4a3f-bbab-0a09d31d246f)

### Login / Signup 

- Upon launching the app users can either chose to log in with an exisiting email
  and password or signup with a new one.

- Utlizies Google Firebases cloud based Authenitcation tool.

![image](https://github.com/user-attachments/assets/b476bcad-b5b7-443b-9281-24b703b5d7fe) ![image](https://github.com/user-attachments/assets/36ddd0bb-2b02-4e9c-816c-1d2c4ba51d5f)


  
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

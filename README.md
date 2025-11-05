# 🧠 SmartInboxAI – AI-Powered Smart Email Assistant  

An **AI-powered email assistant** that helps users **classify emails, summarize content, and draft professional replies** — seamlessly integrated with Gmail and Outlook.  

---

## 🚀 Features
- ✉️ **Smart Email Classification** – Automatically categorizes incoming emails (Work, Personal, Promotions, etc.)  
- 🧾 **AI-Powered Summarization** – Generates concise summaries of long emails using **Gemini API**  
- 🖊️ **Professional Reply Drafts** – Suggests context-aware, professional email replies  
- 🌐 **Browser Extension** – Integrates directly into Gmail/Outlook for one-click actions  
- ⚙️ **REST APIs** – Backend built for seamless integration with frontend and third-party clients  

---

## 🏗️ Tech Stack
**Backend:** Spring Boot, Spring AI  
**Frontend:** React.js  
**AI Engine:** Google Gemini API  
**Integration:** Gmail & Outlook  
**Version Control:** Git & GitHub  

---

## ⚡ Architecture Overview
[User] ⇄ [React Frontend] ⇄ [Spring Boot API] ⇄ [Gemini API]

- Frontend: Handles email UI, displays summaries and suggested replies  
- Backend: Processes requests, calls Gemini AI for NLP tasks  
- Gemini API: Generates summaries and reply suggestions  

---

## 🧩 Installation & Setup

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/HarshitaPatil1808/SmartInboxAI-AI-powered-Smart-Email-Assistant.git
cd SmartInboxAI-AI-powered-Smart-Email-Assistant

2️⃣ Backend Setup (Spring Boot)
cd backend
./mvnw spring-boot:run

3️⃣ Frontend Setup (React)
cd frontend
npm install
npm start

🔐 Environment Variables

Create a .env file in the backend folder and add:

GEMINI_API_KEY=your_api_key_here
EMAIL_PROVIDER_API_KEY=your_email_api_key_here

📦 API Endpoints (Example)
Endpoint	Method	Description
/api/emails/classify	POST	Classify email content
/api/emails/summarize	POST	Generate email summary
/api/emails/reply	POST	Suggest a professional reply
🧠 Example Use Case

Upload or open an email → AI classifies & summarizes → one-click “Generate Reply” → copy or send directly!

👩‍💻 Author

Harshita Katteppagouda Patil
🔗 GitHub Profile

⭐ Contributing

Contributions are welcome!
Feel free to fork this repo, make improvements, and submit a PR 🚀

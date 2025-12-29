Animalphidia - Frontend (vanilla JS)

This frontend is a lightweight, non-React static site that integrates with the Animalphidia Spring backend.

Quick start (dev):
- Open `My-frontend/index.html` in a static file server (or copy files into backend static folder and run backend).

To deploy with your backend (recommended):
- Copy the contents of `My-frontend/` into `My-backend/src/main/resources/static/` and restart the Spring Boot app.

Notes on integration:
- API base: relative `/api` (the backend runs at http://localhost:8081); adjust `window.API_BASE` if needed.
- Auth: login POST /api/auth/login expects { username, password } and responds with accessToken and refreshToken. Access token is stored in-memory; refresh token is stored in localStorage and used by the client to refresh.
- File uploads use POST /api/upload/image multipart form field `file`.

Files created:
- index.html, css/main.css
- js/: api client, auth helpers, router, components, and page scripts

Next steps I can take for you:
- Flesh out UI/UX design and add more pages (dashboard, notifications, collections, moderator/admin views).
- Add client-side validation and better error mapping using your backend's exact 400/401 formats.
- Add automated build script to copy files into `My-backend` static folder.

If you want me to continue, say: "Generate full UI pages + build script" or point to any assets/CSS to match.

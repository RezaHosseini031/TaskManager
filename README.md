<h1 align="center">📋 Task Manager with Focus Timer</h1>

<p align="center">
  <img src="https://raw.githubusercontent.com/RezaHosseini031/TaskManager/refs/heads/main/app/src/main/res/drawable/icon_app.png" width="120" alt="App Icon"/>
</p>

<p align="center">
  <b>An Android Task Manager app focused on clean architecture and scalability</b>
</p>

<hr/>

<h2>📌 Project Overview</h2>

<p>
This project is an Android <b>Task Manager application</b> developed with a strong focus on
<b>Clean Architecture, MVVM pattern, and proper separation of concerns</b>.
In addition to task management, the app includes a <b>Focus Timer</b> to improve productivity.
</p>

<p>
The primary goal of this project is to demonstrate real-world Android development practices,
architecture design, and state management rather than UI-only implementation.
</p>

---

<h2>✨ Features</h2>

<ul>
  <li>✅ Create, edit, and delete tasks</li>
  <li>📂 Separate completed and pending tasks</li>
  <li>⏱️ Focus / Break timer logic</li>
  <li>📊 UI state handling (Loading, Success, Error, Empty)</li>
  <li>🧩 Clean, modular, and scalable architecture</li>
</ul>
---
📱 Screenshots

| Dark | Light |
|------|-------|
| ![](https://raw.githubusercontent.com/RezaHosseini031/TaskManager/refs/heads/main/screenshot/en/dark/screenshot_en_dark.png) | ![](https://raw.githubusercontent.com/RezaHosseini031/TaskManager/refs/heads/main/screenshot/en/light/screenshot_en_light.png) |


<hr></hr>
<h2 tabindex="-1" class="heading-element" dir="auto">Download</h2>
<p dir="auto"><a href="https://github.com/RezaHosseini031/TaskManager/tree/main/app/release/app-release.apk"><img src="https://github.com/gokadzev/Musify/raw/master/repository_files/get-it-on-github.png" alt="Get it on Github" height="80" style="max-width: 100%; height: auto; max-height: 80px;"></a></p>
<hr></hr>

<h2>⚠️ Important Note</h2>

<p>
Due to current network limitations, some features are not implemented or fully tested in this version:
</p>

<ul>
  <li>Network image integration for FloatingActionButton icon</li>
  <li>Full UI testing for this feature</li>
  <li>Detailed code comments (KDoc / inline comments)</li>
  <li>Logging with Timber</li>
  <li>Architecture diagram</li>
</ul>

<p>
<b>Model and ViewModel layers are fully implemented</b> and ready for UI integration.
The project structure allows these features to be added easily in future iterations.
</p>

---

<h2>🧰 Tech Stack</h2>

<ul>
  <li><b>Language:</b> Kotlin</li>
  <li><b>Architecture:</b> MVVM, Repository Pattern, Clean Architecture (data / domain / ui)</li>
  <li><b>UI:</b> XML Layouts, ViewBinding, Material Components</li>
  <li><b>Navigation:</b> Navigation Component, Safe Args</li>
  <li><b>Local Data:</b> Room Database (Entity, DAO, Database)</li>
  <li><b>Networking:</b> Retrofit, OkHttp, Gson Converter</li>
  <li><b>Data Source:</b> Static JSON (hosted on GitHub or stored in assets)</li>
  <li><b>Asynchronous:</b> Kotlin Coroutines, Kotlin Flow, viewModelScope</li>
  <li><b>Dependency Injection:</b> Hilt (Dagger Hilt)</li>
  <li><b>State Management:</b> UiState (sealed class), Result / Resource wrapper</li>
  <li><b>Error Handling:</b> try/catch, network error states, empty state handling</li>
  <li><b>Version Control:</b> Git, GitHub</li>
</ul>

---

<h2>🚀 Future Improvements</h2>

<ul>
  <li>Complete UI integration for remaining features</li>
  <li>Add Timber logging</li>
  <li>Improve documentation with KDoc and inline comments</li>
  <li>Add architecture diagram</li>
  <li>Extend test coverage</li>
</ul>

<hr/>

<h1 align="center">📋 مدیریت کارها با تایمر تمرکز</h1>

<h2>📌 معرفی پروژه</h2>

<p>
این پروژه یک اپلیکیشن <b>مدیریت کار</b> برای اندروید است که با تمرکز ویژه بر
<b>معماری تمیز، الگوی MVVM و جداسازی صحیح لایه‌ها</b> توسعه داده شده است.
در کنار مدیریت تسک‌ها، یک <b>تایمر تمرکز (Focus Timer)</b> نیز در برنامه پیاده‌سازی شده است.
</p>

<p>
هدف اصلی این پروژه نمایش مهارت‌های واقعی توسعه اندروید، طراحی معماری و مدیریت وضعیت‌هاست،
نه صرفاً پیاده‌سازی ظاهری رابط کاربری.
</p>

---

<h2>✨ قابلیت‌ها</h2>

<ul>
  <li>✅ ایجاد، ویرایش و حذف تسک‌ها</li>
  <li>📂 تفکیک کارهای انجام‌شده و انجام‌نشده</li>
  <li>⏱️ منطق تایمر تمرکز و استراحت</li>
  <li>📊 مدیریت وضعیت‌های رابط کاربری (در حال بارگذاری، خطا، خالی، موفق)</li>
  <li>🧩 ساختار تمیز، ماژولار و قابل توسعه</li>
</ul>
---
📱 اسکرین‌شات‌ها

🌙 تم تاریک
![Dark Persian](https://raw.githubusercontent.com/RezaHosseini031/TaskManager/refs/heads/main/screenshot/fa/dark/screenshot_fa_dark.png)

---

<h2>⚠️ نکته مهم</h2>

<p>
به‌دلیل محدودیت‌های فعلی اینترنت، برخی بخش‌ها در این نسخه تکمیل نشده‌اند:
</p>

<ul>
  <li>اتصال تصویر شبکه‌ای به آیکون FloatingActionButton</li>
  <li>تست کامل رابط کاربری برای این قابلیت</li>
  <li>کامنت‌گذاری کامل داخل کد (KDoc / Inline Comments)</li>
  <li>لاگ‌گیری با Timber</li>
  <li>نمودار معماری</li>
</ul>

<p>
با این حال، <b>لایه‌های Model و ViewModel به‌صورت کامل پیاده‌سازی شده‌اند</b>
و پروژه از نظر معماری و منطق بیزینسی کامل و آماده توسعه است.
</p>

---

<h2>🧰 تکنولوژی‌های استفاده‌شده</h2>

<ul>
  <li><b>زبان:</b> Kotlin</li>
  <li><b>معماری:</b> MVVM، Repository Pattern، Clean Architecture</li>
  <li><b>رابط کاربری:</b> XML، ViewBinding، Material Components</li>
  <li><b>ناوبری:</b> Navigation Component، Safe Args</li>
  <li><b>داده محلی:</b> Room (Entity، DAO، Database)</li>
  <li><b>شبکه:</b> Retrofit، OkHttp، Gson</li>
  <li><b>همزمانی:</b> Coroutines، Flow، viewModelScope</li>
  <li><b>تزریق وابستگی:</b> Hilt</li>
  <li><b>مدیریت وضعیت:</b> UiState، Result Wrapper</li>
  <li><b>کنترل نسخه:</b> Git، GitHub</li>
</ul>

---

<h2>🚀 برنامه‌های آینده</h2>

<ul>
  <li>تکمیل کامل رابط کاربری</li>
  <li>افزودن لاگ‌گیری با Timber</li>
  <li>بهبود مستندات و کامنت‌ها</li>
  <li>اضافه کردن نمودار معماری</li>
  <li>افزودن تست‌های بیشتر</li>
</ul>

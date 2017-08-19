Wishmaster - клиент для имиджборда <a href="2ch.hk">2ch.hk</a> на Android
=========================
Скачать <a href="https://github.com/koresuniku/Wishmaster-v3/releases/download/v3.0.2-alpha/wishmaster-v3.0.2-alpha.apk">последний_релиз.apk</a>
-------------------------
Для работы приложения нужен android 4.1 и выше.

![](https://image.ibb.co/cworsF/main.jpg) ![](https://image.ibb.co/bOLAma/main_btrd.jpg) ![](https://image.ibb.co/mPt2zv/b_board.jpg) ![](https://image.ibb.co/fZyfma/b_thread.jpg) ![](https://image.ibb.co/kqXeev/gallery.jpg) ![](https://image.ibb.co/mp2qma/video.jpg)
______________________________________
Проект написан на архитектуре MVC и плавно рефакторится под MVP. Использованы технологии и библиотеки:
* Kotlin как основной язык
* OkHttp3 + Retrofit2 + Gson
* SQLite
* Android Support Tools (CardView, ViewPager, DrawerLayout...)
* Glide
* ExoPlayer (ExoMedia)
* Jsoup
* EventBus
* ButterKnife
* RxJava2, RxAndroid
# 

**Возможности**:
* Просмотр всех разделов сайта и тредов
* Просмотр картинок и видео **из коробки**
* Удобная навигация по ответам к постам)
#

**TODO list core**:
- [ ] Перевести проект на полноценную MVP
- [ ] Применить внедрение зависимостей посредством Dagger2
- [ ] Применить RxJava в Interactor'ах Model слоя
- [ ] Внедрить DataBinding
- [ ] Покрыть Local и Instrumented тестами

**TODO list performance and features:**
- [ ] Оптимизировать ListView
- [ ] Добавить управление кешем картинок
- [ ] Добавить Expandable TextView
- [ ] Добавить постинг и обработать ответы сервера
- [ ] Добавить сохрание файлов с возможность выбора папки сохранения
- [ ] Добавить скрытие тредов
- [ ] Добавить поисковую строку
- [ ] Обработать особенности каждой борды (флаги для /ukr/ и /po/, WebView для шапок тематики...)
- [ ] Выделение и буферизация текста
- [ ] История посещения тредов
- [ ] Фоновое обновление тредов
- [ ] Кеш и сохранение тредов
- [ ] Добавить автоскрытие и написание правил автоскрытия
- [ ] Добавить темы оформления
- [ ] Приклеить больше анимаций
- [ ] Вывести в DrawerLayout сводку по тредам
- [ ] Настроить автопроверку обновлений

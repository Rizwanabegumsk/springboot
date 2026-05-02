-- Seed Authors
INSERT INTO author (name, nationality) VALUES ('George Orwell', 'British');
INSERT INTO author (name, nationality) VALUES ('J.K. Rowling', 'British');
INSERT INTO author (name, nationality) VALUES ('Stephen King', 'American');
INSERT INTO author (name, nationality) VALUES ('Haruki Murakami', 'Japanese');
INSERT INTO author (name, nationality) VALUES ('Ernest Hemingway', 'American');
INSERT INTO author (name, nationality) VALUES ('Gabriel Garcia Marquez', 'Colombian');
INSERT INTO author (name, nationality) VALUES ('Virginia Woolf', 'British');
INSERT INTO author (name, nationality) VALUES ('Leo Tolstoy', 'Russian');
INSERT INTO author (name, nationality) VALUES ('Mark Twain', 'American');
INSERT INTO author (name, nationality) VALUES ('Agatha Christie', 'British');

-- Seed Books
INSERT INTO book (title, genre, published_year, isbn, author_id) VALUES ('1984', 'Dystopian', 1949, '978-0451524935', 1);
INSERT INTO book (title, genre, published_year, isbn, author_id) VALUES ('Animal Farm', 'Political Satire', 1945, '978-0451526342', 1);
INSERT INTO book (title, genre, published_year, isbn, author_id) VALUES ('Harry Potter and the Sorcerer''s Stone', 'Fantasy', 1997, '978-0590353427', 2);
INSERT INTO book (title, genre, published_year, isbn, author_id) VALUES ('The Shining', 'Horror', 1977, '978-0307743657', 3);
INSERT INTO book (title, genre, published_year, isbn, author_id) VALUES ('Norwegian Wood', 'Romance', 1987, '978-0375704079', 4);
INSERT INTO book (title, genre, published_year, isbn, author_id) VALUES ('The Old Man and the Sea', 'Fiction', 1952, '978-0684801223', 5);
INSERT INTO book (title, genre, published_year, isbn, author_id) VALUES ('One Hundred Years of Solitude', 'Magical Realism', 1967, '978-0060883287', 6);
INSERT INTO book (title, genre, published_year, isbn, author_id) VALUES ('To the Lighthouse', 'Modernism', 1927, '978-0156907392', 7);
INSERT INTO book (title, genre, published_year, isbn, author_id) VALUES ('War and Peace', 'Historical Fiction', 1869, '978-0199232178', 8);
INSERT INTO book (title, genre, published_year, isbn, author_id) VALUES ('The Adventures of Tom Sawyer', 'Adventure', 1876, '978-0143039334', 9);

FROM postgres:9.6.12
COPY demographic_information_test.sql /demographic_information_test.sql
COPY wait-for-it.sh /wait-for-it.sh
COPY import_database.sh /import_database.sh
RUN chmod +x /wait-for-it.sh
RUN chmod +x /import_database.sh
CMD ["/import_database.sh"]

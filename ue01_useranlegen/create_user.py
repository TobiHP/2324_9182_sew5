"""
Als Login-Name wird der Familienname verwendet:
– Nur Kleinbuchstaben.
– Alle Sonderzeichen (z.B. Umlaute) werden ersetzt (zB aus Ä wird ae, aus ß wird ss).
– Akzente müssen entfernt werden.
– Leerzeichen im Namen werden durch Unterstrich ersetzt.
– Bei mehreren gleichen Namen werden die Zahlen 1,2,3, ... angehängt (Beispiel: maier, maier1, maier2, ...).
• Das Passwort sollte zufällig gewählt werden.
• Für folgende Teilaufgaben sollten Tests enthalten sein:
– Sonderzeichen im Benutzernamen – Doppelte Benutzernamen
"""
import logging
import random

from openpyxl.reader.excel import load_workbook

logger = logging.getLogger("create_user_logger")


def read_excel(file_path: str):
    """
    Reads an Excel-File and yields the output per line
    :param file_path: Excel file
    :return: tuple of class, classroom, teacher
    """

    logger.debug(f"reading from: {file_path}")

    try:
        workbook = load_workbook(file_path, read_only=True)
    except Exception:
        logger.error("Error: File " + file_path + " not Found!")
        raise Exception("File \"" + file_path + "\" not Found!")

    worksheet = workbook[workbook.sheetnames[0]]

    for row in worksheet.iter_rows(min_row=1):
        first_name = row[0].value
        last_name = row[1].value
        group_name = row[2].value
        class_name = row[3].value

        if first_name is not None and last_name is not None and group_name is not None:
            logger.debug(f"yielding: {first_name} {last_name} {group_name}")
            yield first_name, last_name, group_name, class_name
        else:
            logger.error(f"malformed user entry for: {row}")


def create_users(filename: str):
    logger.debug(f"generating users from: {filename}")

    last_names = []

    excel = read_excel(filename)
    excel.__next__()
    for row in excel:
        first_name = row[0]
        last_name = row[1]
        group_name = row[2].lower()
        class_name = row[3].lower() if row[3] else None
        user_name = first_name + last_name

        if last_name in last_names:
            logger.debug("lastname '" + last_name + "' already exists -> appending number")
            last_names.append(last_name)
            last_name = last_name + str(last_names.count(last_name)-1)
        else:
            last_names.append(last_name)

        logger.debug(f"yielding class user: {first_name}")

        from create_class import generate_password
        if class_name:
            home_directory = f"/home/klassen/{class_name}/{last_name}/"
        else:
            home_directory = f"/home/lehrer/{last_name}/"
        yield (home_directory,
               first_name,
               group_name,
               class_name,
               user_name,
               generate_password(first_name, str(row[1]), str(row[2])))


if __name__ == '__main__':
    for user in create_users("Namen.xlsx"):
        print(user)

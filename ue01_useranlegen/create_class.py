'''
    - ein Bash-Script2 mit allen notwendigen Schritten/Befehlen zum Erzeugen der Benutzer3
    – ein Bash-Script zum Löschen der angelegten Benutzer
    – eine Liste mit Usernamen und Passwörtern zum Verteilen an die unterrichtenden Lehrer
    – ein Logfile mit sinnvollen Angaben
'''
'''
    – Einlesen (in Liste/Generator oder ähnliches)
    – Aufbereiten/Nachbessern:
        ∗ PW erzeugen11
        ∗ bei echten Usernamen: Korrektur des Namens (Umlaute), doppelte Namen, etc. ∗ Ordnernamen
    – Ausgabe
'''

import logging
import argparse

from openpyxl import load_workbook


def read_excel(file_path: str):
    """
    Reads an Excel-File and yields the output per line
    :param file_path: Excel file
    :return: tuple of class, classroom, teacher
    """

    logging.debug("reading from:" + file_path)

    workbook = load_workbook(file_path, read_only=True)
    worksheet = workbook[workbook.sheetnames[0]]

    for row in worksheet.iter_rows(min_row=1):
        class_name = row[0].value
        class_room = row[1].value
        class_teacher = row[2].value

        if class_name is not None and class_room is not None and class_teacher is not None:
            logging.debug(class_name, class_room, class_teacher)
            yield class_name, class_room, class_teacher


def write_script(data, filename: str):
    # home_directory, username, main_group, groups = data
    with open(filename, "w") as script:
        for entry in data:
            home_directory, username, main_group, groups = entry
            print(entry)
            script.write("useradd "
                         " -d " + home_directory +
                         " -c " + username +
                         " -m " +
                         " -g " + main_group +
                         " -G " + groups +
                         " -s /bin/bash " + username +
                         "\n"
                         )


def create_class_users_script(filename: str):
    write_script(create_class_users(filename), "user_script.sh")


def create_class_users(filename: str):
    home_directory = "/home/klassen"
    groups = "cdrom,plugdev,sambashare"

    excel = read_excel(filename)
    excel.__next__()
    for row in excel:
        class_name = row[0].lower()
        yield home_directory, class_name, class_name, groups


def parse_args():
    """
    The necessary commands to make the command line tools usable
    :return:
    """
    parser = argparse.ArgumentParser()
    parser.add_argument("filename")
    parser.add_argument('-v', "--verbosity", help="increase output verbosity", action="store_true")
    parser.add_argument("-q", "--quiet", help="decrease output verbosity", action="store_true")

    args = parser.parse_args()

    if args.filename:
        create_class_users_script(args.filename)

    # todo revisit
    if args.verbosity:
        print("verbosity turned on")
        logging.basicConfig(level=5)
    elif args.quiet:
        logging.basicConfig(level=0)


if __name__ == '__main__':
    parse_args()

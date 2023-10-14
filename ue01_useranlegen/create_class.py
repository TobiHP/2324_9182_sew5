import logging
import argparse
import random

from openpyxl import load_workbook


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


def write_class_script(data, filename: str):
    """
    Writes data consisting of user entries in a given file
    :param data: generator object (home_directory, username, main_group, groups)
    :param filename: file to be written
    :return: bash script for user creation
    """

    groups = "cdrom,plugdev,sambashare"

    with open(filename, "w") as script, open("passwords.txt", "w") as passwords:
        for entry in data:
            home_directory, username, main_group, password = entry
            script.write("useradd "
                         " -d " + home_directory +
                         " -c " + username +
                         " -m " +
                         " -g " + main_group +
                         " -G " + groups +
                         " -s /bin/bash " + username +
                         "\n"
                         )
            script.write("echo " + username + ":" + password + " | chpasswd\n\n")
            passwords.write("Klasse:   " + username + "\nPasswort: " + password.replace("\\", "") + "\n\n")

        for additional in create_additional_users():
            home_directory, username, main_group = additional
            script.write("useradd "
                         " -d " + home_directory +
                         " -c " + username +
                         " -m " +
                         " -g " + main_group +
                         " -G " + groups +
                         " -s /bin/bash " + username +
                         "\n"
                         )


def create_class_users(filename: str):
    """
    generates user data from a given excel file
    :param filename: excel file
    :return: yield (home_directory, class_name, class_name, groups)
    """
    home_directory = "/home/klassen"
    class_names = []

    excel = read_excel(filename)
    excel.__next__()
    for row in excel:
        class_name = "k" + row[0].lower()
        if class_name in class_names:
            raise Exception("user '" + class_name + "' already exists!")
        else:
            class_names.append(class_name)
            yield (home_directory + "/" + class_name, class_name, class_name
                   , generate_password(class_name, str(row[1]), str(row[2])))


def create_additional_users():
    yield "/home/lehrer", "lehrer", "lehrer"
    yield "/home/lehrer", "seminar", "seminar"


def generate_password(name, room, teacher):
    """
    Das Passwort besteht aus KZRZJZ
        ∗ K(lasse)
        ∗ Z(ufall): ein zufälliges Zeichen aus !%&(),._-=^#5
        ∗ R(aum), ohne dem B für Beamer
        ∗ J(ahrgangsvorstand)
    :return:
    """
    rand_chars = ["!", "%", "&", "(", ")", ",", ".", "_", "-", "=", "^", "#"]

    return (name +
            "\\" + random.choice(rand_chars)
            + room
            + "\\" + random.choice(rand_chars)
            + teacher
            + "\\" + random.choice(rand_chars)
            )


def create_password_file():
    pass


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
        write_class_script(create_class_users(args.filename), "user_script.sh")

    # todo revisit
    if args.verbosity:
        print("verbosity turned on")
        logging.basicConfig(level=5)
    elif args.quiet:
        logging.basicConfig(level=0)


if __name__ == '__main__':
    parse_args()


# TODO: – bei bereits existierenden Benutzern sollte eine Fehlermeldung erfolgen????????????
# TODO: – die Bash-Scripts sollten bei Problemen abbrechen

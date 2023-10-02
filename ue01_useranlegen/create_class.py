'''
    - ein Bash-Script2 mit allen notwendigen Schritten/Befehlen zum Erzeugen der Benutzer3
    – ein Bash-Script zum Löschen der angelegten Benutzer
    – eine Liste mit Usernamen und Passwörtern zum Verteilen an die unterrichtenden Lehrer
    – ein Logfile mit sinnvollen Angaben
'''
import argparse

'''
    – Einlesen (in Liste/Generator oder ähnliches)
    – Aufbereiten/Nachbessern:
        ∗ PW erzeugen11
        ∗ bei echten Usernamen: Korrektur des Namens (Umlaute), doppelte Namen, etc. ∗ Ordnernamen
    – Ausgabe
'''

from openpyxl import load_workbook


def read_excel(file_path: str):
    """
    Reads an Excel-File and yields the output per line
    :param file_path: Excel file
    :return: tuple of class, classroom, teacher
    """
    workbook = load_workbook(file_path, read_only=True)
    worksheet = workbook[workbook.sheetnames[0]]

    for row in worksheet.iter_rows(min_row=1):
        class_name = row[0].value
        class_room = row[1].value
        class_teacher = row[2].value

        if class_name is not None and class_room is not None and class_teacher is not None:
            yield class_name, class_room, class_teacher


def make_parser():
    """
    The necessary commands to make the command line tools usable
    :return:
    """
    parser = argparse.ArgumentParser()
    parser.add_argument("filename")

    args = parser.parse_args()

    if args.filename:
        test_read(args.filename)


def test_read(filename):
    for row in read_excel(filename):
        print(row)


if __name__ == '__main__':
    make_parser()
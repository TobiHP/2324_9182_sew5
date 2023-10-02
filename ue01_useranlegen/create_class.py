'''
    - ein Bash-Script2 mit allen notwendigen Schritten/Befehlen zum Erzeugen der Benutzer3
    – ein Bash-Script zum Löschen der angelegten Benutzer
    – eine Liste mit Usernamen und Passwörtern zum Verteilen an die unterrichtenden Lehrer
    – ein Logfile mit sinnvollen Angaben
'''

from openpyxl import load_workbook


def read_excel(file_path: str):
    """
    Reads an Excel-File and yields the output per line
    :param file_path: Excel file
    :return:
    """
    workbook = load_workbook(file_path, read_only=True)
    worksheet = workbook[workbook.sheetnames[0]]

    for row in worksheet.iter_rows(min_row=1):
        class_name = row[0].value
        class_room = row[1].value
        class_teacher = row[2].value

        if class_name is not None and class_room is not None and class_teacher is not None:
            yield class_name, class_room, class_teacher


if __name__ == '__main__':
    for row in read_excel("Klassenraeume_2023.xlsx"):
        print(row)
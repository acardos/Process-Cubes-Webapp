# Generated by Django 2.2.4 on 2019-08-17 19:25

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('event_log', '__first__'),
        ('process_cube_view', '0001_initial'),
    ]

    operations = [
        migrations.AlterUniqueTogether(
            name='mapping',
            unique_together={('e_attribute', 'pcv')},
        ),
    ]

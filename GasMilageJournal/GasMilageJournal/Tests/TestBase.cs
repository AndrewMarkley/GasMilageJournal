using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.IO;
using System.Linq;
using System.Web;
using GasMileageJournal.Models.Data;
using Ionic.Zip;
using NUnit.Framework;

namespace GasMileageJournal.Tests
{
    public class TestBase
    {
        [TestFixtureSetUp]
        public void Setup()
        {
            var baseDirectory = AppDomain.CurrentDomain.BaseDirectory;
            var appDataDirectory = Path.Combine(baseDirectory.Replace("\\bin", ""), "App_Data");

            AppDomain.CurrentDomain.SetData("DataDirectory", appDataDirectory);

            var zipPath = Path.Combine(appDataDirectory, "GasMileageJournal-test.zip");
            var dataPathDb = Path.Combine(appDataDirectory, "GasMileageJournal-test.mdf");

            if (!File.Exists(dataPathDb)) {
                using (var zip = ZipFile.Read(zipPath)) {
                    zip.ExtractAll(appDataDirectory, ExtractExistingFileAction.DoNotOverwrite);
                }
            }

            DataContext.ConnectionStringName = "Test";

            Database.SetInitializer(new DropCreateDatabaseAlways<DataContext>());

            var context = new DataContext();
            //User.Seed(context);
            //Subject.Seed(context);
            //Task.Seed(context);
            //CalendarEvent.Seed(context);
        }

        [TestFixtureTearDown]
        public void TearDown()
        {
        }
    }
}
using System;
using System.Linq;
using System.Net;
using System.Web.Http;
using GasMileageJournal.Models.Cars;
using GasMileageJournal.Models.Data;
using GasMileageJournal.Models.Users;
using System.Runtime.Serialization.Json;
using System.IO;
using System.Text;

namespace GasMileageJournal.Areas.Api.Controllers
{
    public class BaseApiController<T> : ApiController where T : class
    {
        public String ApiKey
        {
            get { return GetHeader("ApiKey"); }
        }

        public String UserId
        {
            get { return UserManager.FindByApiKey(ApiKey).Id; }
        }

        public String GetHeader(String name)
        {
            return (Request.Headers.All(t => t.Key != name))
                ? null
                : Request.Headers.GetValues(name).First();
        }

        public static string Serialize(T obj)
        {
            var serializer = new DataContractJsonSerializer(obj.GetType());
            var ms = new MemoryStream();
            serializer.WriteObject(ms, obj);
            var retVal = Encoding.UTF8.GetString(ms.ToArray());
            return retVal;
        }

        public static T Deserialize(string json)
        {
            var obj = Activator.CreateInstance<T>();
            var ms = new MemoryStream(Encoding.Unicode.GetBytes(json));
            var serializer = new DataContractJsonSerializer(obj.GetType());
            obj = (T) serializer.ReadObject(ms);
            ms.Close();
            return obj;
        }

        [HttpGet]
        public String Save()
        {
            try {
                var apiKey = GetHeader("ApiKey");
                var user = UserManager.Context.Users.FirstOrDefault(t => t.ApiKey == apiKey);

                if (user == null) {
                    throw new HttpResponseException(HttpStatusCode.NotFound);
                }

                var json = GetHeader("Object");
                var data = Deserialize(json);

                using (var manager = new CarManager())
                using (var context = new DataContext()) {
                    var dataModel = (IDataModel) data;

                    var entity = (IDataModel) manager.GetById<T>(dataModel.Id);

                    if (entity == null) {
                        context.Set<T>().Add(data);
                    }
                    else {
                        manager.Save(data);
                    }

                    context.SaveChanges();

                    var result = (IDataModel) manager.GetById<T>(dataModel.Id);

                    return result.Id;
                }
            }
            catch (Exception) {
                throw new HttpResponseException(HttpStatusCode.BadRequest);
            }
        }
    }
}
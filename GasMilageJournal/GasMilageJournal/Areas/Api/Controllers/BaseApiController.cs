using System;
using System.Linq;
using System.Web.Http;
using GasMileageJournal.Models.Users;
using System.Runtime.Serialization.Json;
using System.IO;
using System.Text;

namespace GasMileageJounal.Areas.Api.Controllers
{
    public class BaseApiController : ApiController
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

        public static string Serialize<T>(T obj)
        {
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(obj.GetType());
            MemoryStream ms = new MemoryStream();
            serializer.WriteObject(ms, obj);
            string retVal = Encoding.UTF8.GetString(ms.ToArray());
            return retVal;
        }

        public static T Deserialize<T>(string json)
        {
            T obj = Activator.CreateInstance<T>();
            MemoryStream ms = new MemoryStream(Encoding.Unicode.GetBytes(json));
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(obj.GetType());
            obj = (T)serializer.ReadObject(ms);
            ms.Close();
            return obj;
        }
    }
}